package com.projeto.e_commerce.catalog;

import java.util.UUID;


public record ResponseCategoryDto(
    UUID id,
    String name,
    String description,
    Boolean active

) {

    public ResponseCategoryDto(Category category) {
        this(category.getId(), category.getName(), category.getDescription(), category.getActive());
    }

}
