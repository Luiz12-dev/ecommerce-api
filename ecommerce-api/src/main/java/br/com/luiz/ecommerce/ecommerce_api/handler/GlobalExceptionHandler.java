package br.com.luiz.ecommerce.ecommerce_api.handler;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex){

        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex){

        String erros = ex.getBindingResult().getFieldErrors().stream()
            .map(erro -> erro.getField() + ": "+ erro.getDefaultMessage())
            .collect(Collectors.joining("; "));

            return ResponseEntity.badRequest().body("Erro de validação: " + erros);
    }
}
