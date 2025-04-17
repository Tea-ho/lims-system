package com.lims.lims_study.domain.test.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReceiptInfo {

    private Long id;
    private String receiptDetails;
    private boolean requiresApproval;
    private Long approvalId;

    public ReceiptInfo(String receiptDetails, boolean requiresApproval) {
        this.receiptDetails = receiptDetails;
        this.requiresApproval = requiresApproval;
    }
}