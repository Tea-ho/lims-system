package com.lims.lims_study.domain.test.model;

import com.lims.lims_study.application.test.dto.TestUpdateDto;

public class RequestInfo {

    private Long id;
    private String title;
    private String description;
    private boolean requiresApproval;

    public RequestInfo(String title, String description, boolean requiresApproval) {
        this.title = title;
        this.description = description;
        this.requiresApproval = requiresApproval;
    }

    public void updateRequestInfo(TestUpdateDto dto){
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.requiresApproval = dto.isRequiresApproval();
    }

    // getter and setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRequiresApproval() {
        return requiresApproval;
    }

    public void setRequiresApproval(boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }
}