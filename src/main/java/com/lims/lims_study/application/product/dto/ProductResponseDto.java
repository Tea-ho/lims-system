package com.lims.lims_study.application.product.dto;

public class ProductResponseDto {
    private final Long id;
    private final String name;
    private final String description;
    private final String createdAt;
    private final String updatedAt;

    public ProductResponseDto(Long id, String name, String description, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
}