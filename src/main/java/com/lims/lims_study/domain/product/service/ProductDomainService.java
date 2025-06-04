package com.lims.lims_study.domain.product.service;

import com.lims.lims_study.application.product.dto.ProductUpdateDto;
import com.lims.lims_study.domain.product.model.Product;
import com.lims.lims_study.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductDomainService implements IProductService {

    private final ProductRepository productRepository;
    private final ProductValidator productValidator;

    @Override
    public void createProduct(Product product) {
        productValidator.checkProductNameBlank(product);
        productValidator.checkDuplicateProductName(product);
        this.productRepository.insert(product);
    }

    @Override
    public void updateProduct(Long productId, ProductUpdateDto dto) {
        Product existing = productValidator.validateProductExists(productId);
        existing.updateProductInfo(dto);
        this.productRepository.update(existing);
    }

    @Override
    public void deleteProduct(Long productId) {
        productValidator.validateProductExists(productId);
        productRepository.delete(productId);
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public List<Product> searchProducts(String name) {
        return productRepository.searchByName(name);
    }
}