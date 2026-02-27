package com.projeto.e_commerce.Exception;

import java.time.LocalDateTime;

public record StandartErrorDto(
    LocalDateTime timeStamp,
    Integer status,
    String error,
    String message,
    String path
) {

}
