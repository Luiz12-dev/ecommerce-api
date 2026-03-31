package com.projeto.e_commerce.catalog;

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
@RequestMapping("api/v1/categories")
@Tag(name = "Categorias", description = "Gerenciamento de categorias de produtos")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Criar categoria", description = "Cria uma nova categoria (requer perfil ADMIN)")
    @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso")
    @ApiResponse(responseCode = "409", description = "Categoria com esse nome já existe")
    @PostMapping
    public ResponseEntity<ResponseCategoryDto> createCategory(@RequestBody @Valid RequestCategoryDto req) {
        ResponseCategoryDto res = categoryService.createCategory(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @Operation(summary = "Listar categorias", description = "Lista todas as categorias ativas com paginação (público)")
    @GetMapping
    public ResponseEntity<Page<ResponseCategoryDto>> findAll(Pageable pageable) {
        Page<ResponseCategoryDto> categories = categoryService.findAll(pageable);
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Buscar categoria por ID", description = "Retorna os detalhes de uma categoria (público)")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseCategoryDto> findById(@PathVariable UUID id) {
        ResponseCategoryDto category = categoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Atualizar categoria", description = "Atualiza nome e descrição (requer perfil ADMIN)")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseCategoryDto> update(@PathVariable UUID id, @RequestBody @Valid RequestCategoryDto req) {
        ResponseCategoryDto updated = categoryService.update(id, req);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Desativar categoria", description = "Soft delete — marca como inativa (requer perfil ADMIN)")
    @ApiResponse(responseCode = "204", description = "Categoria desativada com sucesso")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
