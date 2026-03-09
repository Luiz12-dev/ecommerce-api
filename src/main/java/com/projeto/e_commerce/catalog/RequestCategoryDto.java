package com.projeto.e_commerce.catalog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestCategoryDto(

    @NotBlank(message = "O nome nao pode ser nulo !")
    @Size(min = 3, max = 100, message = "O nome deve ser entre 3 e 100 caracteres")
    String name,

    @Size(max = 255, message = "O maximo de caracteres é 255 ")
    String description

) {

}
