package com.lims.lims_study.application.approval.dto;

import com.lims.lims_study.domain.approval.model.ApprovalStatus;
import com.lims.lims_study.domain.test.model.TestStage;

import java.util.List;

public class ApprovalResponseDto {
    private final Long id;
    private final ApprovalStatus status;
    private final Long version;           // 동시성 제어를 위한 버전 정보
    private final ApprovalRequestDto request;
    private final List<ApprovalSignDto> signs;
    private final String createdAt;
    private final String updatedAt;

    public ApprovalResponseDto(Long id, ApprovalStatus status, Long version, ApprovalRequestDto request,
                               List<ApprovalSignDto> signs, String createdAt, String updatedAt) {
        this.id = id;
        this.status = status;
        this.version = version;
        this.request = request;
        this.signs = signs;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static class ApprovalRequestDto {
        private final Long requesterId;
        private final String comment;
        private final String createdAt;
        private final String updatedAt;

        public ApprovalRequestDto(Long requesterId, String comment, String createdAt, String updatedAt) {
            this.requesterId = requesterId;
            this.comment = comment;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        // Getters
        public Long getRequesterId() { return requesterId; }
        public String getComment() { return comment; }
        public String getCreatedAt() { return createdAt; }
        public String getUpdatedAt() { return updatedAt; }
    }

    public static class ApprovalSignDto {
        private final Long id;
        private final Long approverId;
        private final Long targetId;
        private final TestStage stage;
        private final ApprovalStatus status;
        private final String comment;
        private final String createdAt;
        private final String updatedAt;

        public ApprovalSignDto(Long id, Long approverId, Long targetId, TestStage stage, ApprovalStatus status,
                               String comment, String createdAt, String updatedAt) {
            this.id = id;
            this.approverId = approverId;
            this.targetId = targetId;
            this.stage = stage;
            this.status = status;
            this.comment = comment;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        // Getters
        public Long getId() { return id; }
        public Long getApproverId() { return approverId; }
        public Long getTargetId() { return targetId; }
        public TestStage getStage() { return stage; }
        public ApprovalStatus getStatus() { return status; }
        public String getComment() { return comment; }
        public String getCreatedAt() { return createdAt; }
        public String getUpdatedAt() { return updatedAt; }
    }

    // Getters
    public Long getId() { return id; }
    public ApprovalStatus getStatus() { return status; }
    public Long getVersion() { return version; }
    public ApprovalRequestDto getRequest() { return request; }
    public List<ApprovalSignDto> getSigns() { return signs; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
}