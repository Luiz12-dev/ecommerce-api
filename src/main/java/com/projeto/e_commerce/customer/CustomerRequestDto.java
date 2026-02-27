package com.projeto.e_commerce.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerRequestDto(

    @NotBlank(message = "O nome nao pode ser vazio")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caractes")
    String name,

    @Email(message = "Formato de e-mail inválido")
    @NotBlank(message = "O email é obrigatório")
    String email
) {

}
