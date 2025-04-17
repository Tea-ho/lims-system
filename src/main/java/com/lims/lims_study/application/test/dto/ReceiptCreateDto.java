package com.lims.lims_study.application.test.dto;

import lombok.Getter;
import lombok.Setter;

public class ReceiptCreateDto {
    private String receiptDetails;
    private boolean requiresApproval;

    public String getReceiptDetails() {
        return receiptDetails;
    }

    public boolean isRequiresApproval() {
        return requiresApproval;
    }
}