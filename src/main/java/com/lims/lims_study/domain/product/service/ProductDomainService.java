package com.lims.lims_study.domain.product.service;

import com.lims.lims_study.domain.product.model.Product;
import com.lims.lims_study.domain.product.repository.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductDomainService implements IProductService {

    private final ProductMapper productMapper;
    private final ProductValidator productValidator;

    @Override
    @Transactional
    public void createProduct(Product product) {
        productValidator.validateCreate(product);
        productMapper.insert(product);
    }

    @Override
    @Transactional
    public void updateProduct(Long productId, Product product) {
        productValidator.validateProductExists(productId);
        product.setId(productId);
        product.setUpdatedAt(LocalDateTime.now());
        productMapper.update(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        productValidator.validateProductExists(productId);
        productMapper.delete(productId);
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return productMapper.findById(productId);
    }

    @Override
    public List<Product> searchProducts(String name) {
        return productMapper.searchByName(name);
    }
}