package com.lims.lims_study.domain.approval.model;

import java.time.LocalDateTime;

public class Approval {
    private Long id;
    private ApprovalStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Approval(ApprovalStatus status) {
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(ApprovalStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public ApprovalStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}