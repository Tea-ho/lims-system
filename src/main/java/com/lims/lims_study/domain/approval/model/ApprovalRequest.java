package com.lims.lims_study.domain.approval.model;

import java.time.LocalDateTime;

public class ApprovalRequest {
    private Long id;
    private Long approvalId;
    private Long requesterId;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ApprovalRequest(Long approvalId, Long requesterId, String comment) {
        this.approvalId = approvalId;
        this.requesterId = requesterId;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getApprovalId() { return approvalId; }
    public Long getRequesterId() { return requesterId; }
    public String getComment() { return comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}