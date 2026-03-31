package com.projeto.e_commerce.order;

import java.math.BigDecimal;
import java.util.UUID;

public record ResponseOrderItemDto(
    UUID id,
    UUID productId,
    String productName,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal subtotal
) {

    public ResponseOrderItemDto(OrderItem item) {
        this(
            item.getId(),
            item.getProduct().getId(),
            item.getProduct().getName(),
            item.getQuantity(),
            item.getUnitPrice(),
            item.getSubtotal()
        );
    }
}
