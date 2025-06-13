package com.lims.lims_study.domain.product.service;

import com.lims.lims_study.domain.product.model.Product;
import com.lims.lims_study.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductValidator {
    private final ProductRepository productRepository;

    public void checkProductNameBlank(Product product){
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new IllegalArgumentException("ProductRepository name cannot be empty");
        }
    }

    public void checkDuplicateProductName(Product product){
        this.productRepository.findByName(product.getName())
                .ifPresent(p -> { throw new IllegalArgumentException("ProductRepository name already exists: " + p.getName()); });
    }

    public Product validateProductExists(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("ProductRepository not found: " + productId));
    }
}