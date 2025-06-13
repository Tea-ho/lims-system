package com.lims.lims_study.domain.approval.model;

import com.lims.lims_study.domain.test.model.TestStage;

import java.time.LocalDateTime;

public class ApprovalSign {
    private Long id;
    private Long approvalId;
    private Long approverId;
    private Long targetId;
    private TestStage stage;
    private ApprovalStatus status;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ApprovalSign(Long approvalId, Long approverId, Long targetId, TestStage stage, ApprovalStatus status, String comment) {
        this.approvalId = approvalId;
        this.approverId = approverId;
        this.targetId = targetId;
        this.stage = stage;
        this.status = status;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(ApprovalStatus status, String comment) {
        this.status = status;
        this.comment = comment;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getApprovalId() { return approvalId; }
    public Long getApproverId() { return approverId; }
    public Long getTargetId() { return targetId; }
    public TestStage getStage() { return stage; }
    public ApprovalStatus getStatus() { return status; }
    public String getComment() { return comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}