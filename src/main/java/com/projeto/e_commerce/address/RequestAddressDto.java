package com.projeto.e_commerce.address;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RequestAddressDto(

    @NotBlank(message = "A rua é obrigatória")
    @Size(max = 200, message = "A rua pode ter no máximo 200 caracteres")
    String street,

    @NotBlank(message = "O número é obrigatório")
    @Size(max = 20, message = "O número pode ter no máximo 20 caracteres")
    String number,

    @Size(max = 100, message = "O complemento pode ter no máximo 100 caracteres")
    String complement,

    @NotBlank(message = "O bairro é obrigatório")
    @Size(max = 100, message = "O bairro pode ter no máximo 100 caracteres")
    String neighborhood,

    @NotBlank(message = "A cidade é obrigatória")
    @Size(max = 100, message = "A cidade pode ter no máximo 100 caracteres")
    String city,

    @NotBlank(message = "O estado é obrigatório")
    @Size(min = 2, max = 2, message = "O estado deve ter exatamente 2 caracteres (ex: SP, RJ)")
    String state,

    @NotBlank(message = "O CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "O CEP deve estar no formato 00000-000 ou 00000000")
    String zipCode,

    @NotNull(message = "O ID do cliente é obrigatório")
    UUID customerId
) {
}
