package com.projeto.e_commerce.auth;

import java.time.LocalDateTime;
import java.util.UUID;

public record RegisterResponseDto(
    UUID id,
    String fullName,
    String email,
    UserRole role,
    LocalDateTime createdAt
) {

    public RegisterResponseDto(AppUser user) {
        this(
            user.getId(),
            user.getFullName(),
            user.getEmail(),
            user.getRole(),
            user.getCreatedAt()
        );
    }
}
