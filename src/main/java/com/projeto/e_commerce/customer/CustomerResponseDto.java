package com.projeto.e_commerce.customer;
import java.util.UUID;

import java.time.LocalDateTime;

public record CustomerResponseDto(
    UUID id,
    String name,
    String email,
    LocalDateTime createdAt
) {

    public CustomerResponseDto(Customer customer ){
        this(customer.getId(), customer.getName(), customer.getEmail(), customer.getCreatedAt());
    }

}
