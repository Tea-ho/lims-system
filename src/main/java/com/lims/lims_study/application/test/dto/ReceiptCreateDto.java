package com.lims.lims_study.application.test.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceiptCreateDto {
    private String receiptDetails;
    private boolean requiresApproval = false;
}