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

    // 결과입력
    void inputResult(Long testId, ResultInfo resultInfo);

    // 결과승인 요청
    void moveToResultApproval(Long testId, ApprovalCreateDto approvalDto);

    // 결과승인 -> 결과입력 (반려)
    void moveBackToResultInput(Long testId, Long approvalId, ApprovalSignUpdateDto updateDto);

    // 결과승인 -> 완료 (승인)
    void moveToCompleted(Long testId, Long approvalId, ApprovalSignUpdateDto updateDto);
}