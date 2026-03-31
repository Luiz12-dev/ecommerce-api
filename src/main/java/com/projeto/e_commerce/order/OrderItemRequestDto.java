package com.projeto.e_commerce.order;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequestDto(

    @NotNull(message = "O ID do produto é obrigatório")
    UUID productId,

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade mínima é 1")
    Integer quantity
) {
}
