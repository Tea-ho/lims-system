package com.lims.lims_study.application.test.dto;

public class TestUpdateDto {
    private String title;
    private String description;
    private String priority;
    private boolean requiresApproval;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
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

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setRequiresApproval(boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }
}