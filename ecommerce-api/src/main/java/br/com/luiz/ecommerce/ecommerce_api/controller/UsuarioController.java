package br.com.luiz.ecommerce.ecommerce_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.luiz.ecommerce.ecommerce_api.dtos.UserRequestDTO;
import br.com.luiz.ecommerce.ecommerce_api.dtos.UserResponseDTO;
import br.com.luiz.ecommerce.ecommerce_api.service.UserService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
    
    private final UserService userService;

    public UsuarioController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> criarUsuario(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponse = userService.criarUsuario(userRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }
    


}
