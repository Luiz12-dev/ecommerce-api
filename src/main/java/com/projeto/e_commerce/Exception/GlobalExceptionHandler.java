package com.projeto.e_commerce.Exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandartErrorDto> illegalArgumentException(IllegalArgumentException ex, HttpServletRequest req){
        StandartErrorDto error = new StandartErrorDto(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Regra de Negócio Violada",
            ex.getMessage(),
            req.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandartErrorDto>handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req){
        String details = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining(", "));

        StandartErrorDto error = new StandartErrorDto(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Erro de Validaçao",
            details,
            req.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandartErrorDto>handleGeneric(Exception ex, HttpServletRequest req ){
        StandartErrorDto error = new StandartErrorDto(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Erro Interno no Servidor",
            "Ocorreu um erro inesperado. Tente novamente mais tarde. ",
            req.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
