package com.projeto.e_commerce.auth;

public record AuthResponseDto(
    String accessToken,
    String type
) {

    public AuthResponseDto(String accessToken) {
        this(accessToken, "Bearer");
    }
}
