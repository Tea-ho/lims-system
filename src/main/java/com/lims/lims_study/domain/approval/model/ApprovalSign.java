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

    // MyBatis용 기본 생성자
    public ApprovalSign() {
    }

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
    public void setId(Long id) { this.id = id; }

    public Long getApprovalId() { return approvalId; }
    public void setApprovalId(Long approvalId) { this.approvalId = approvalId; }

    public Long getApproverId() { return approverId; }
    public void setApproverId(Long approverId) { this.approverId = approverId; }

    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }

    public TestStage getStage() { return stage; }
    public void setStage(TestStage stage) { this.stage = stage; }

    public ApprovalStatus getStatus() { return status; }
    public void setStatus(ApprovalStatus status) { this.status = status; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}