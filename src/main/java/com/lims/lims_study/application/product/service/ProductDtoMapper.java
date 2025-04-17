package com.lims.lims_study.application.product.service;

import com.lims.lims_study.application.product.dto.ProductResponseDto;
import com.lims.lims_study.domain.product.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductDtoMapper {
    public ProductResponseDto toResponseDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCreatedAt().toString(),
                product.getUpdatedAt() != null ? product.getUpdatedAt().toString() : null
        );
    }
}
