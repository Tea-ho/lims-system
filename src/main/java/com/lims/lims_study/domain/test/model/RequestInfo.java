package com.lims.lims_study.domain.test.model;

import com.lims.lims_study.application.test.dto.TestUpdateDto;
import com.lims.lims_study.global.exception.BusinessException;
import com.lims.lims_study.global.exception.ErrorCode;

public class RequestInfo {

    private Long id;
    private String title;
    private String description;
    private String priority;
    private boolean requiresApproval;

    // MyBatis용 기본 생성자
    public RequestInfo() {
    }

    public RequestInfo(String title, String description) {
        validateInputs(title, description);
        this.title = title;
        this.description = description;
        this.priority = "NORMAL";
        this.requiresApproval = false;
    }

    public RequestInfo(String title, String description, boolean requiresApproval) {
        validateInputs(title, description);
        this.title = title;
        this.description = description;
        this.priority = "NORMAL";
        this.requiresApproval = requiresApproval;
    }

    public RequestInfo(String title, String description, String priority, boolean requiresApproval) {
        validateInputs(title, description);
        this.title = title;
        this.description = description;
        this.priority = priority != null ? priority : "NORMAL";
        this.requiresApproval = requiresApproval;
    }

    private void validateInputs(String title, String description) {
        if (title == null || title.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "제목은 필수입니다.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "설명은 필수입니다.");
        }
    }

    public void updateRequestInfo(TestUpdateDto dto){
        if (dto.getTitle() != null && !dto.getTitle().trim().isEmpty()) {
            this.title = dto.getTitle();
        }
        if (dto.getDescription() != null) {
            this.description = dto.getDescription();
        }
        if (dto.getPriority() != null) {
            this.priority = dto.getPriority();
        }
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isRequiresApproval() {
        return requiresApproval;
    }

    public void setRequiresApproval(boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }

}