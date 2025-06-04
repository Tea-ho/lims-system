package com.lims.lims_study.application.approval.dto;

import com.lims.lims_study.domain.approval.model.ApprovalStatus;

public class ApprovalUpdateDto {
    private ApprovalStatus status;
    private String comment;

    public ApprovalStatus getStatus() { return status; }
    public String getComment() { return comment; }
}
