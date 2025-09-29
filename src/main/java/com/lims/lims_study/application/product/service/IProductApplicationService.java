package com.lims.lims_study.application.product.service;

import com.lims.lims_study.application.product.dto.ProductCreateDto;
import com.lims.lims_study.application.product.dto.ProductResponseDto;
import com.lims.lims_study.application.product.dto.ProductSearchDto;
import com.lims.lims_study.application.product.dto.ProductUpdateDto;

import java.util.List;

public interface IProductApplicationService {
    ProductResponseDto createProduct(ProductCreateDto dto);
    ProductResponseDto updateProduct(Long productId, ProductUpdateDto dto);
    void deleteProduct(Long productId);
    ProductResponseDto getProduct(Long productId);
    List<ProductResponseDto> searchProducts(ProductSearchDto dto);
    List<ProductResponseDto> getAllProducts();
}
