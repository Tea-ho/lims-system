package com.lims.lims_study.application.product.service;

import com.lims.lims_study.application.product.dto.ProductResponseDto;
import com.lims.lims_study.domain.product.model.Product;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductDtoMapper {
    public ProductResponseDto toResponseDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCreatedAt().toString(),
                mapUpdatedAt(product.getUpdatedAt())
        );
    }

    private String mapUpdatedAt(Object updatedAt) {
        return Optional.ofNullable(updatedAt)
                .map(Object::toString)
                .orElse(null);
    }
}
