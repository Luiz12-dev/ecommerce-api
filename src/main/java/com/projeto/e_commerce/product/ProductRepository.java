package com.projeto.e_commerce.product;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findAllByActiveTrue();

    List<Product> findAllByCategoryIdAndActiveTrue(UUID categoryId);

    List<Product> findAllByNameContainingIgnoreCaseAndActiveTrue(String name);
}
