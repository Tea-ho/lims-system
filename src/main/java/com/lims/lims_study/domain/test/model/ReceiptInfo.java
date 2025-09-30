package com.lims.lims_study.domain.test.model;

public class ReceiptInfo {

    private Long id;
    private String receiptDetails;
    private boolean requiresApproval;

    // MyBatis용 기본 생성자
    public ReceiptInfo() {
    }

    public ReceiptInfo(String receiptDetails, boolean requiresApproval) {
        this.receiptDetails = receiptDetails;
        this.requiresApproval = requiresApproval;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReceiptDetails() {
        return receiptDetails;
    }

    public void setReceiptDetails(String receiptDetails) {
        this.receiptDetails = receiptDetails;
    }

    public boolean isRequiresApproval() {
        return requiresApproval;
    }

    public void setRequiresApproval(boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }


}