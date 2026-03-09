package com.projeto.e_commerce.catalog;

import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryReppository){
        this.categoryRepository = categoryReppository;
    }

    public ResponseCategoryDto createCategory(RequestCategoryDto req){

        if(categoryRepository.findByName(req.name()).isPresent()) {
            throw new IllegalArgumentException("Esse nome já foi criado !");
        }

        Category category = Category.builder()
        .name(req.name())
        .description(req.description()).build();

        Category savedCategory = categoryRepository.save(category);


        return new ResponseCategoryDto(savedCategory);
    }

    
}
