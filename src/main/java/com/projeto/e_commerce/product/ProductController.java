package com.projeto.e_commerce.product;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/products")
@Tag(name = "Produtos", description = "Gerenciamento de produtos do catálogo")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Criar produto", description = "Cadastra um novo produto vinculado a uma categoria (requer ADMIN)")
    @ApiResponse(responseCode = "201", description = "Produto criado com sucesso")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    @PostMapping
    public ResponseEntity<ResponseProductDto> create(@RequestBody @Valid RequestProductDto req) {
        ResponseProductDto product = productService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @Operation(summary = "Listar produtos", description = "Lista todos os produtos ativos com paginação (público)")
    @GetMapping
    public ResponseEntity<Page<ResponseProductDto>> findAll(Pageable pageable) {
        Page<ResponseProductDto> products = productService.findAll(pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Buscar produto por ID", description = "Retorna detalhes de um produto (público)")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseProductDto> findById(@PathVariable UUID id) {
        ResponseProductDto product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Filtrar por categoria", description = "Lista produtos ativos de uma categoria (público)")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ResponseProductDto>> findByCategory(@PathVariable UUID categoryId, Pageable pageable) {
        Page<ResponseProductDto> products = productService.findByCategory(categoryId, pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Buscar por nome", description = "Pesquisa produtos pelo nome (case insensitive, público)")
    @GetMapping("/search")
    public ResponseEntity<Page<ResponseProductDto>> searchByName(@RequestParam String name, Pageable pageable) {
        Page<ResponseProductDto> products = productService.searchByName(name, pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Atualizar produto", description = "Atualiza dados do produto (requer ADMIN)")
    @ApiResponse(responseCode = "404", description = "Produto ou categoria não encontrados")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseProductDto> update(@PathVariable UUID id, @RequestBody @Valid RequestProductDto req) {
        ResponseProductDto updated = productService.update(id, req);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Desativar produto", description = "Soft delete — marca como inativo (requer ADMIN)")
    @ApiResponse(responseCode = "204", description = "Produto desativado com sucesso")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
