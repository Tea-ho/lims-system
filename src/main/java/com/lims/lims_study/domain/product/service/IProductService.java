package com.lims.lims_study.domain.product.service;

import com.lims.lims_study.application.product.dto.ProductUpdateDto;
import com.lims.lims_study.domain.product.model.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    void createProduct(Product product);
    void updateProduct(Long productId, ProductUpdateDto dto);
    void deleteProduct(Long productId);
    Optional<Product> findById(Long productId);
    List<Product> searchProducts(String name);
    List<Product> findAllProducts();
}