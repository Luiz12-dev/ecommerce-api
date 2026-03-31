package com.projeto.e_commerce.product;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestProductDto(

    @NotBlank(message = "O nome do produto é obrigatório")
    @Size(min = 2, max = 150, message = "O nome deve ter entre 2 e 150 caracteres")
    String name,

    @Size(max = 2000, message = "A descrição pode ter no máximo 2000 caracteres")
    String description,

    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
    BigDecimal price,

    @NotNull(message = "A quantidade em estoque é obrigatória")
    @Min(value = 0, message = "A quantidade em estoque não pode ser negativa")
    Integer stockQuantity,

    String imageUrl,

    @NotNull(message = "O ID da categoria é obrigatório")
    UUID categoryId
) {
}
