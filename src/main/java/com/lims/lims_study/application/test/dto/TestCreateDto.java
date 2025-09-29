package com.lims.lims_study.application.test.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TestCreateDto {
    @NotBlank(message = "제목은 필수입니다")
    private String title;

    private String description;

    @NotNull(message = "제품 ID는 필수입니다")
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setRequiresApproval(boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }
}