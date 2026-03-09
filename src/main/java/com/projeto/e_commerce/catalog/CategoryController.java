package com.projeto.e_commerce.catalog;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @PostMapping()
    public ResponseEntity<ResponseCategoryDto> createCategory(@RequestBody @Valid RequestCategoryDto req) {

        ResponseCategoryDto res = categoryService.createCategory(req);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
      
    }
    
    
}
