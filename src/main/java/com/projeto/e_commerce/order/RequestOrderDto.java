package com.projeto.e_commerce.order;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RequestOrderDto(

    @NotNull(message = "O ID do cliente é obrigatório")
    UUID customerId,

    @NotNull(message = "O ID do endereço é obrigatório")
    UUID addressId,

    @NotEmpty(message = "O pedido deve conter pelo menos um item")
    @Valid
    List<OrderItemRequestDto> items
) {
}
