package com.projeto.e_commerce.exception;

import java.time.LocalDateTime;

public record StandardErrorDto(
    LocalDateTime timeStamp,
    Integer status,
    String error,
    String message,
    String path
) {

}
