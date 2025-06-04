package com.lims.lims_study.application.test.dto;

public class TestCreateDto {
    private String title;
    private String description;
    private Long productId;
    private boolean requiresApproval;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getProductId() {
        return productId;
    }

    public boolean isRequiresApproval() {
        return requiresApproval;
    }
}