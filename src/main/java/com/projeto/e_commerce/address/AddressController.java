package com.projeto.e_commerce.address;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/addresses")
@Tag(name = "Endereços", description = "Gerenciamento de endereços de entrega dos clientes")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Operation(summary = "Cadastrar endereço", description = "Cria um novo endereço vinculado a um cliente")
    @ApiResponse(responseCode = "201", description = "Endereço criado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @PostMapping
    public ResponseEntity<ResponseAddressDto> create(@RequestBody @Valid RequestAddressDto req) {
        ResponseAddressDto address = addressService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(address);
    }

    @Operation(summary = "Listar endereços do cliente", description = "Retorna todos os endereços de um cliente")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ResponseAddressDto>> findByCustomer(@PathVariable UUID customerId) {
        List<ResponseAddressDto> addresses = addressService.findByCustomer(customerId);
        return ResponseEntity.ok(addresses);
    }

    @Operation(summary = "Buscar endereço por ID")
    @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseAddressDto> findById(@PathVariable UUID id) {
        ResponseAddressDto address = addressService.findById(id);
        return ResponseEntity.ok(address);
    }

    @Operation(summary = "Atualizar endereço")
    @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseAddressDto> update(@PathVariable UUID id, @RequestBody @Valid RequestAddressDto req) {
        ResponseAddressDto updated = addressService.update(id, req);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Deletar endereço")
    @ApiResponse(responseCode = "204", description = "Endereço removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
