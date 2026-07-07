package com.projeto.e_commerce.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.e_commerce.address.Address;
import com.projeto.e_commerce.address.AddressRepository;
import com.projeto.e_commerce.customer.Customer;
import com.projeto.e_commerce.customer.CustomerRepository;
import com.projeto.e_commerce.exception.BusinessRuleException;
import com.projeto.e_commerce.exception.ResourceNotFoundException;
import com.projeto.e_commerce.product.Product;
import com.projeto.e_commerce.product.ProductRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        AddressRepository addressRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public ResponseOrderDto create(RequestOrderDto req) {
        // 1. Validar se o cliente existe
        Customer customer = customerRepository.findById(req.customerId())
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + req.customerId()));

        // 2. Validar se o endereço existe e pertence ao cliente
        Address address = addressRepository.findById(req.addressId())
            .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado com ID: " + req.addressId()));

        if (!address.getCustomer().getId().equals(customer.getId())) {
            throw new BusinessRuleException("O endereço informado não pertence ao cliente");
        }

        // 3. Criar o pedido
        Order order = Order.builder()
            .customer(customer)
            .address(address)
            .totalAmount(BigDecimal.ZERO)
            .items(new ArrayList<>())
            .build();

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();

        // 4. Para cada item do pedido: validar produto, verificar estoque e decrementar
        for (OrderItemRequestDto itemReq : req.items()) {
            Product product = productRepository.findById(itemReq.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + itemReq.productId()));

            if (!product.getActive()) {
                throw new BusinessRuleException("O produto '" + product.getName() + "' está inativo e não pode ser comprado");
            }
            
            if (product.getStockQuantity() < itemReq.quantity()) {
                throw new BusinessRuleException(
                    "Estoque insuficiente para o produto '" + product.getName()
                    + "'. Disponível: " + product.getStockQuantity()
                    + ", Solicitado: " + itemReq.quantity()
                );
            }

            // Decrementar estoque
            product.setStockQuantity(product.getStockQuantity() - itemReq.quantity());
            productRepository.save(product);

            // Calcular subtotal do item (preço unitário × quantidade)
            BigDecimal unitPrice = product.getPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(itemReq.quantity()));

            OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(itemReq.quantity())
                .unitPrice(unitPrice)
                .subtotal(subtotal)
                .build();

            items.add(orderItem);
            totalAmount = totalAmount.add(subtotal);
        }

        // 5. Definir total e salvar pedido com itens (cascade)
        order.setTotalAmount(totalAmount);
        order.getItems().addAll(items);

        Order savedOrder = orderRepository.save(order);

        return new ResponseOrderDto(savedOrder);
    }

    @Transactional(readOnly = true)
    public ResponseOrderDto findById(UUID id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com ID: " + id));

        return new ResponseOrderDto(order);
    }

    @Transactional(readOnly = true)
    public List<ResponseOrderDto> findByCustomer(UUID customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Cliente não encontrado com ID: " + customerId);
        }

        return orderRepository.findAllByCustomerId(customerId).stream()
            .map(ResponseOrderDto::new)
            .collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public ResponseOrderDto updateStatus(UUID id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com ID: " + id));

        validateStatusTransition(order.getStatus(), newStatus);

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        return new ResponseOrderDto(updatedOrder);
    }

    @Transactional
    public ResponseOrderDto cancel(UUID id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com ID: " + id));

        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new BusinessRuleException("Não é possível cancelar um pedido que já foi enviado ou entregue");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessRuleException("Este pedido já foi cancelado");
        }

        // Reestabelecer estoque dos produtos
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order cancelledOrder = orderRepository.save(order);

        return new ResponseOrderDto(cancelledOrder);
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        if (currentStatus == OrderStatus.CANCELLED) {
            throw new BusinessRuleException("Não é possível alterar o status de um pedido cancelado");
        }

        if (currentStatus == OrderStatus.DELIVERED) {
            throw new BusinessRuleException("Não é possível alterar o status de um pedido já entregue");
        }

        boolean validTransition = switch (currentStatus) {
            case PENDING -> newStatus == OrderStatus.CONFIRMED;
            case CONFIRMED -> newStatus == OrderStatus.SHIPPED;
            case SHIPPED -> newStatus == OrderStatus.DELIVERED;
            default -> false;
        };

        if (!validTransition) {
            throw new BusinessRuleException(
                "Transição de status inválida: " + currentStatus + " → " + newStatus
                + ". Fluxo válido: PENDING → CONFIRMED → SHIPPED → DELIVERED"
            );
        }
    }
}
