package com.lims.lims_study.domain.product.service;

import com.lims.lims_study.domain.product.model.Product;
import com.lims.lims_study.domain.product.repository.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductValidator {
    private final ProductMapper productMapper;

    public void validateCreate(Product product) {
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        productMapper.findByName(product.getName())
                .ifPresent(p -> { throw new IllegalArgumentException("Product name already exists: " + p.getName()); });
    }

    public Product validateProductExists(Long productId) {
        return productMapper.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
    }
}