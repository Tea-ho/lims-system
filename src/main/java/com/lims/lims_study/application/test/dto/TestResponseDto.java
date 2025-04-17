package com.lims.lims_study.application.test.dto;

import com.lims.lims_study.application.product.dto.ProductResponseDto;
import com.lims.lims_study.application.user.dto.UserResponseDto;

public class TestResponseDto {
    private final Long id;
    private final RequestInfoDto requestInfo;
    private final ReceiptInfoDto receiptInfo;
    private final ResultInfoDto resultInfo;
    private final CompletionInfoDto completionInfo;
    private final UserResponseDto user;
    private final ProductResponseDto product;
    private final String stage;
    private final String createdAt;
    private final String updatedAt;

    public TestResponseDto(Long id, RequestInfoDto requestInfo, ReceiptInfoDto receiptInfo,
                           ResultInfoDto resultInfo, CompletionInfoDto completionInfo,
                           UserResponseDto user, ProductResponseDto product,
                           String stage, String createdAt, String updatedAt) {
        this.id = id;
        this.requestInfo = requestInfo;
        this.receiptInfo = receiptInfo;
        this.resultInfo = resultInfo;
        this.completionInfo = completionInfo;
        this.user = user;
        this.product = product;
        this.stage = stage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public RequestInfoDto getRequestInfo() { return requestInfo; }
    public ReceiptInfoDto getReceiptInfo() { return receiptInfo; }
    public ResultInfoDto getResultInfo() { return resultInfo; }
    public CompletionInfoDto getCompletionInfo() { return completionInfo; }
    public UserResponseDto getUser() { return user; }
    public ProductResponseDto getProduct() { return product; }
    public String getStage() { return stage; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    public static class RequestInfoDto {
        private final String title;
        private final String description;
        private final boolean requiresApproval;

        public RequestInfoDto(String title, String description, boolean requiresApproval) {
            this.title = title;
            this.description = description;
            this.requiresApproval = requiresApproval;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public boolean isRequiresApproval() {
            return requiresApproval;
        }
    }

    public static class ReceiptInfoDto {
        private final String receiptDetails;
        private final boolean requiresApproval;

        public ReceiptInfoDto(String receiptDetails, boolean requiresApproval) {
            this.receiptDetails = receiptDetails;
            this.requiresApproval = requiresApproval;
        }
    }

    public static class ResultInfoDto {
        private final String resultData;
        private final boolean requiresApproval;

        public ResultInfoDto(String resultData, boolean requiresApproval) {
            this.resultData = resultData;
            this.requiresApproval = requiresApproval;
        }
    }

    public static class CompletionInfoDto {
        private final String completionNotes;
        private final boolean requiresApproval;

        public CompletionInfoDto(String completionNotes, boolean requiresApproval) {
            this.completionNotes = completionNotes;
            this.requiresApproval = requiresApproval;
        }
    }
}