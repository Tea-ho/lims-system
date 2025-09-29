package com.lims.lims_study.application.approval.dto;

import com.lims.lims_study.domain.approval.model.ApprovalStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApprovalSignUpdateDto {
    private ApprovalStatus status;
    private String comment;

    public ApprovalSignUpdateDto(ApprovalStatus status, String comment) {
        this.status = status;
        this.comment = comment;
    }
}