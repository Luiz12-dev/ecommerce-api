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

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ResponseProductDto> create(@RequestBody @Valid RequestProductDto req) {
        ResponseProductDto product = productService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping
    public ResponseEntity<Page<ResponseProductDto>> findAll(Pageable pageable) {
        Page<ResponseProductDto> products = productService.findAll(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseProductDto> findById(@PathVariable UUID id) {
        ResponseProductDto product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ResponseProductDto>> findByCategory(@PathVariable UUID categoryId, Pageable pageable) {
        Page<ResponseProductDto> products = productService.findByCategory(categoryId, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ResponseProductDto>> searchByName(@RequestParam String name, Pageable pageable) {
        Page<ResponseProductDto> products = productService.searchByName(name, pageable);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseProductDto> update(@PathVariable UUID id, @RequestBody @Valid RequestProductDto req) {
        ResponseProductDto updated = productService.update(id, req);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
