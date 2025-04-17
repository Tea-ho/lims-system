package com.lims.lims_study.application.product.dto;

public class ProductCreateDto {
    private String name;
    private String description;

    public String getName() {return name;}
    public String getDescription() {return description;}

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}