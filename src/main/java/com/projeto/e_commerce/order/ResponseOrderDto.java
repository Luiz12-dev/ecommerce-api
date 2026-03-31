package com.projeto.e_commerce.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ResponseOrderDto(
    UUID id,
    UUID customerId,
    String customerName,
    UUID addressId,
    OrderStatus status,
    BigDecimal totalAmount,
    LocalDateTime createdAt,
    List<ResponseOrderItemDto> items
) {

    public ResponseOrderDto(Order order) {
        this(
            order.getId(),
            order.getCustomer().getId(),
            order.getCustomer().getName(),
            order.getAddress().getId(),
            order.getStatus(),
            order.getTotalAmount(),
            order.getCreatedAt(),
            order.getItems().stream()
                .map(ResponseOrderItemDto::new)
                .toList()
        );
    }
}
