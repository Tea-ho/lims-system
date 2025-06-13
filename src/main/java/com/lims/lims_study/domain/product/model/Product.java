package com.lims.lims_study.domain.product.model;

import com.lims.lims_study.application.product.dto.ProductUpdateDto;

import java.time.LocalDateTime;

public class Product {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public void updateProductInfo(ProductUpdateDto dto){
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.updatedAt = LocalDateTime.now();
    }
}