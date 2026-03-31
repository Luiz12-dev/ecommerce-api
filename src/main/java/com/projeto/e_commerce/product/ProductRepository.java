package com.projeto.e_commerce.product;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Page<Product> findAllByActiveTrue(Pageable pageable);

    Page<Product> findAllByCategoryIdAndActiveTrue(UUID categoryId, Pageable pageable);

    Page<Product> findAllByNameContainingIgnoreCaseAndActiveTrue(String name, Pageable pageable);
}
