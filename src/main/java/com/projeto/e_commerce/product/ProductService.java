package com.projeto.e_commerce.product;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.e_commerce.catalog.Category;
import com.projeto.e_commerce.catalog.CategoryRepository;
import com.projeto.e_commerce.exception.BusinessRuleException;
import com.projeto.e_commerce.exception.ResourceNotFoundException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ResponseProductDto create(RequestProductDto req) {
        Category category = categoryRepository.findById(req.categoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + req.categoryId()));

        if (!category.getActive()) {
            throw new BusinessRuleException("Não é possível vincular um produto a uma categoria inativa");
        }

        Product product = Product.builder()
            .name(req.name())
            .description(req.description())
            .price(req.price())
            .stockQuantity(req.stockQuantity())
            .imageUrl(req.imageUrl())
            .category(category)
            .build();

        Product savedProduct = productRepository.save(product);

        return new ResponseProductDto(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ResponseProductDto> findAll() {
        return productRepository.findAllByActiveTrue().stream()
            .map(ResponseProductDto::new)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResponseProductDto findById(UUID id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));

        return new ResponseProductDto(product);
    }

    @Transactional(readOnly = true)
    public List<ResponseProductDto> findByCategory(UUID categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Categoria não encontrada com ID: " + categoryId);
        }

        return productRepository.findAllByCategoryIdAndActiveTrue(categoryId).stream()
            .map(ResponseProductDto::new)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResponseProductDto> searchByName(String name) {
        return productRepository.findAllByNameContainingIgnoreCaseAndActiveTrue(name).stream()
            .map(ResponseProductDto::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public ResponseProductDto update(UUID id, RequestProductDto req) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));

        Category category = categoryRepository.findById(req.categoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + req.categoryId()));

        if (!category.getActive()) {
            throw new BusinessRuleException("Não é possível vincular um produto a uma categoria inativa");
        }

        product.setName(req.name());
        product.setDescription(req.description());
        product.setPrice(req.price());
        product.setStockQuantity(req.stockQuantity());
        product.setImageUrl(req.imageUrl());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);

        return new ResponseProductDto(updatedProduct);
    }

    @Transactional
    public void delete(UUID id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));

        product.setActive(false);
        productRepository.save(product);
    }
}
