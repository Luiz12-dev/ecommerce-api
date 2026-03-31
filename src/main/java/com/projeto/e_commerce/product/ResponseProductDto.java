package com.projeto.e_commerce.product;

import java.math.BigDecimal;
import java.util.UUID;

public record ResponseProductDto(
    UUID id,
    String name,
    String description,
    BigDecimal price,
    Integer stockQuantity,
    String imageUrl,
    Boolean active,
    UUID categoryId,
    String categoryName
) {

    public ResponseProductDto(Product product) {
        this(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStockQuantity(),
            product.getImageUrl(),
            product.getActive(),
            product.getCategory().getId(),
            product.getCategory().getName()
        );
    }
}
