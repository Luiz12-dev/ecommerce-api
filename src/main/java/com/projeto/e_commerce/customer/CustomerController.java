package com.projeto.e_commerce.customer;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("api/v1/customers")
@Tag(name = "Clientes", description = "Gerenciamento de clientes")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Cadastrar cliente", description = "Cria um novo cliente no sistema")
    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso")
    @ApiResponse(responseCode = "409", description = "E-mail já cadastrado")
    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody @Valid CustomerRequestDto req) {
        CustomerResponseDto customerDto = customerService.createCustomer(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDto);
    }

    @Operation(summary = "Listar clientes", description = "Retorna todos os clientes com paginação")
    @GetMapping
    public ResponseEntity<Page<CustomerResponseDto>> findAll(Pageable pageable) {
        Page<CustomerResponseDto> customers = customerService.findAll(pageable);
        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Buscar cliente por ID")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> findById(@PathVariable UUID id) {
        CustomerResponseDto customer = customerService.findById(id);
        return ResponseEntity.ok(customer);
    }

    @Operation(summary = "Atualizar cliente")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> update(@PathVariable UUID id, @RequestBody @Valid CustomerRequestDto req) {
        CustomerResponseDto updated = customerService.update(id, req);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Deletar cliente")
    @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
