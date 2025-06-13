package com.lims.lims_study.domain.test.service;

import com.lims.lims_study.application.approval.dto.ApprovalCreateDto;
import com.lims.lims_study.application.approval.dto.ApprovalSignUpdateDto;
import com.lims.lims_study.domain.test.model.*;

import java.util.List;

public interface ITestStateService {
    void moveToReceipt(Long testId, ReceiptInfo receiptInfo);
    void moveBackToRequest(Long testId);
    void moveToReceiptApproval(Long testId, ApprovalCreateDto approvalDto);
    void moveBackToReceipt(Long testId, Long approvalId, ApprovalSignUpdateDto updateDto);
    void moveToInputResult(Long testId, Long approvalId, ApprovalSignUpdateDto updateDto);
}