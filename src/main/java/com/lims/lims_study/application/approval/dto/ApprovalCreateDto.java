package com.lims.lims_study.application.approval.dto;

import com.lims.lims_study.domain.test.model.TestStage;

import java.util.List;

public class ApprovalCreateDto {
    private Long requesterId;
    private String comment;
    private List<ApprovalSignDto> signs;

    public static class ApprovalSignDto {
        private Long approverId;
        private Long targetId;
        private TestStage stage;

        public Long getApproverId() { return approverId; }
        public void setApproverId(Long approverId) { this.approverId = approverId; }
        public Long getTargetId() { return targetId; }
        public void setTargetId(Long targetId) { this.targetId = targetId; }
        public TestStage getStage() { return stage; }
        public void setStage(TestStage stage) { this.stage = stage; }
    }

    public Long getRequesterId() { return requesterId; }
    public String getComment() { return comment; }
    public List<ApprovalSignDto> getSigns() { return signs; }
}