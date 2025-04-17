package com.lims.lims_study.application.test.dto;

public class TestUpdateDto {
    private String title;
    private String description;
    private boolean requiresApproval;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRequiresApproval() {
        return requiresApproval;
    }
}