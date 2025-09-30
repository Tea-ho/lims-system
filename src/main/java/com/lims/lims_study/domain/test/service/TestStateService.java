package com.lims.lims_study.domain.test.service;

import com.lims.lims_study.application.approval.dto.ApprovalCreateDto;
import com.lims.lims_study.application.approval.dto.ApprovalResponseDto;
import com.lims.lims_study.application.approval.dto.ApprovalSignUpdateDto;
import com.lims.lims_study.domain.approval.service.IApprovalService;
import com.lims.lims_study.domain.test.model.ReceiptInfo;
import com.lims.lims_study.domain.test.model.Test;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestStateService implements ITestStateService {
    private final ITestCrudService testCrudService;
    private final ITestReceiptCrudService testReceiptCrudService;
    private final TestValidator testValidator;
    private final IApprovalService approvalService;

    @Override
    @Transactional
    public void moveToReceipt(Long testId, ReceiptInfo receiptInfo) {
        System.out.println("🔧 moveToReceipt started - Test ID: " + testId);

        Test test = testValidator.validateTestExists(testId, testCrudService);
        System.out.println("🔧 Test found - Current stage: " + test.getStage());

        testReceiptCrudService.insert(receiptInfo);
        System.out.println("🔧 ReceiptInfo inserted - ID: " + receiptInfo.getId());

        test.setReceiptInfoId(receiptInfo.getId());
        System.out.println("🔧 Receipt Info ID set to test: " + test.getReceiptInfoId());

        test.moveToNextStage();
        System.out.println("🔧 Stage moved to: " + test.getStage());

        testCrudService.updateTest(test);
        System.out.println("🔧 Test updated in DB");
    }

    @Override
    @Transactional
    public void moveBackToRequest(Long testId) {
        Test test = testValidator.validateTestExists(testId, testCrudService);
        test.moveToPreviousStage();
        testCrudService.updateTest(test);
    }

    @Override
    @Transactional
    public void moveToReceiptApproval(Long testId, ApprovalCreateDto approvalDto) {
        Test test = testValidator.validateTestExists(testId, testCrudService);
        ReceiptInfo receiptInfo = testValidator.validateReciptInfoExists(test.getReceiptInfoId(), testReceiptCrudService);

        if (receiptInfo.isRequiresApproval()) {
            approvalService.createApproval(approvalDto);
        }

        testReceiptCrudService.update(receiptInfo);
        test.moveToNextStage();
        testCrudService.updateTest(test);
    }

    @Override
    @Transactional
    public void moveBackToReceipt(Long testId, Long approvalId, ApprovalSignUpdateDto updateDto) {
        Test test = testValidator.validateTestExists(testId, testCrudService);

        updateApprovalSign(approvalId, updateDto);

        test.moveToPreviousStage();
        testCrudService.updateTest(test);
    }

    @Override
    @Transactional
    public void moveToInputResult(Long testId, Long approvalId, ApprovalSignUpdateDto updateDto) {
        Test test = testValidator.validateTestExists(testId, testCrudService);

        updateApprovalSign(approvalId, updateDto);

        test.moveToNextStage();
        testCrudService.updateTest(test);
    }

    private void updateApprovalSign(Long approvalId, ApprovalSignUpdateDto updateDto){
        ApprovalResponseDto approval = approvalService.getApproval(approvalId);
        for (ApprovalResponseDto.ApprovalSignDto signDto : approval.getSigns()) {
            approvalService.updateApprovalSign(approvalId, signDto.getId(), updateDto);
        }
    }

}