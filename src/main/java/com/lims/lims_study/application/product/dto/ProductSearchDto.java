package com.lims.lims_study.application.product.dto;

public class ProductSearchDto {
    private String name;

    public ProductSearchDto() {
    }

    public ProductSearchDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
