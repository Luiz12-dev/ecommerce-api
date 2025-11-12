package br.com.luiz.ecommerce.ecommerce_api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
    @NotBlank
    String nome,

    @NotBlank
    @Email
    String email,
    
    @NotBlank
    @Size(min = 6)
    String senha
    

) {

}
