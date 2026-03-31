package com.projeto.e_commerce.order;

import jakarta.validation.constraints.NotNull;

public record UpdateStatusDto(

    @NotNull(message = "O novo status é obrigatório")
    OrderStatus status
) {
}
