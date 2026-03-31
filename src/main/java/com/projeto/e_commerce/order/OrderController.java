package com.projeto.e_commerce.order;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/orders")
@Tag(name = "Pedidos", description = "Gerenciamento de pedidos do e-commerce")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Criar pedido", description = "Cria um novo pedido com validação de estoque e cálculo automático de totais")
    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Estoque insuficiente ou regra de negócio violada")
    @ApiResponse(responseCode = "404", description = "Cliente, endereço ou produto não encontrado")
    @PostMapping
    public ResponseEntity<ResponseOrderDto> create(@RequestBody @Valid RequestOrderDto req) {
        ResponseOrderDto order = orderService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @Operation(summary = "Buscar pedido por ID", description = "Retorna detalhes completos do pedido com itens")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseOrderDto> findById(@PathVariable UUID id) {
        ResponseOrderDto order = orderService.findById(id);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Listar pedidos do cliente", description = "Retorna pedidos paginados de um cliente")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<ResponseOrderDto>> findByCustomer(@PathVariable UUID customerId, Pageable pageable) {
        Page<ResponseOrderDto> orders = orderService.findByCustomer(customerId, pageable);
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Atualizar status", description = "Transição de status do pedido (PENDING → CONFIRMED → SHIPPED → DELIVERED)")
    @ApiResponse(responseCode = "400", description = "Transição de status inválida")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseOrderDto> updateStatus(@PathVariable UUID id, @RequestBody @Valid UpdateStatusDto req) {
        ResponseOrderDto order = orderService.updateStatus(id, req.status());
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Cancelar pedido", description = "Cancela o pedido e reestabelece o estoque dos produtos")
    @ApiResponse(responseCode = "400", description = "Pedido já enviado/entregue não pode ser cancelado")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ResponseOrderDto> cancel(@PathVariable UUID id) {
        ResponseOrderDto order = orderService.cancel(id);
        return ResponseEntity.ok(order);
    }
}
