package com.lims.lims_study.domain.test.model;

import java.time.LocalDateTime;

public class Test {
    private Long id;
    private Long requestInfoId;
    private Long receiptInfoId;
    private Long resultInfoId;
    private Long completionInfoId;
    private Long userId;
    private Long productId;
    private String stage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Test(Long userId, Long productId, RequestInfo requestInfo) {
        this.userId = userId;
        this.productId = productId;
        this.requestInfoId = requestInfo.getId();
        this.stage = TestStage.REQUEST.name();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 상태 전이 메서드
    public void moveToNextStage() {
        TestStage next = TestStage.valueOf(stage).next();
        if (next == null) throw new IllegalStateException("No next stage for " + stage);
        this.stage = next.name();
        this.updatedAt = LocalDateTime.now();
    }

    public void moveToPreviousStage() {
        TestStage prev = TestStage.valueOf(stage).previous();
        if (prev == null) throw new IllegalStateException("No previous stage for " + stage);
        this.stage = prev.name();
        this.updatedAt = LocalDateTime.now();
    }

    public void completeTest() {
        if (!TestStage.COMPLETED.name().equals(stage)) {
            throw new IllegalStateException("Can only complete test in COMPLETED stage");
        }
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRequestInfoId() { return requestInfoId; }
    public void setRequestInfoId(Long requestInfoId) { this.requestInfoId = requestInfoId; }
    public Long getReceiptInfoId() { return receiptInfoId; }
    public void setReceiptInfoId(Long receiptInfoId) { this.receiptInfoId = receiptInfoId; }
    public Long getResultInfoId() { return resultInfoId; }
    public void setResultInfoId(Long resultInfoId) { this.resultInfoId = resultInfoId; }
    public Long getCompletionInfoId() { return completionInfoId; }
    public void setCompletionInfoId(Long completionInfoId) { this.completionInfoId = completionInfoId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getStage() { return stage; }
    public void setStage(String stage) { this.stage = stage; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}