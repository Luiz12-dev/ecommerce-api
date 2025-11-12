package br.com.luiz.ecommerce.ecommerce_api.dtos;

import java.time.LocalDateTime;

public record UserResponseDTO(

    Long id,

    String nome,
    
    String email,

    LocalDateTime dataCriacao,
    
    LocalDateTime dataAtualizacao

) {

}
