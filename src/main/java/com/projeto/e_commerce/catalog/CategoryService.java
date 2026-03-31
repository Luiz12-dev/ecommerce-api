package com.projeto.e_commerce.catalog;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.e_commerce.exception.DuplicateResourceException;
import com.projeto.e_commerce.exception.ResourceNotFoundException;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ResponseCategoryDto createCategory(RequestCategoryDto req) {
        if (categoryRepository.findByName(req.name()).isPresent()) {
            throw new DuplicateResourceException("Já existe uma categoria com o nome: " + req.name());
        }

        Category category = Category.builder()
            .name(req.name())
            .description(req.description())
            .build();

        Category savedCategory = categoryRepository.save(category);

        return new ResponseCategoryDto(savedCategory);
    }

    @Transactional(readOnly = true)
    public Page<ResponseCategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAllByActiveTrue(pageable)
            .map(ResponseCategoryDto::new);
    }

    @Transactional(readOnly = true)
    public ResponseCategoryDto findById(UUID id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + id));

        return new ResponseCategoryDto(category);
    }

    @Transactional
    public ResponseCategoryDto update(UUID id, RequestCategoryDto req) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + id));

        categoryRepository.findByName(req.name())
            .filter(existing -> !existing.getId().equals(id))
            .ifPresent(existing -> {
                throw new DuplicateResourceException("Já existe uma categoria com o nome: " + req.name());
            });

        category.setName(req.name());
        category.setDescription(req.description());

        Category updatedCategory = categoryRepository.save(category);

        return new ResponseCategoryDto(updatedCategory);
    }

    @Transactional
    public void delete(UUID id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + id));

        category.setActive(false);
        categoryRepository.save(category);
    }
}
