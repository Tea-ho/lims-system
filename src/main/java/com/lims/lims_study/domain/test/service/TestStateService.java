package com.lims.lims_study.domain.test.service;

import com.lims.lims_study.application.approval.dto.ApprovalCreateDto;
import com.lims.lims_study.application.approval.dto.ApprovalResponseDto;
import com.lims.lims_study.application.approval.dto.ApprovalSignUpdateDto;
import com.lims.lims_study.domain.approval.service.IApprovalService;
import com.lims.lims_study.domain.test.model.ReceiptInfo;
import com.lims.lims_study.domain.test.model.ResultInfo;
import com.lims.lims_study.domain.test.model.Test;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestStateService implements ITestStateService {
    private final ITestCrudService testCrudService;
    private final ITestReceiptCrudService testReceiptCrudService;
    private final ITestResultCrudService testResultCrudService;
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

        // 결재 필요 여부에 따라 단계 분기
        if (receiptInfo.isRequiresApproval()) {
            // 결재 필요: REQUEST -> RECEIPT
            test.moveToNextStage();
            System.out.println("🔧 Stage moved to RECEIPT (approval required): " + test.getStage());
        } else {
            // 결재 불필요: REQUEST -> RECEIPT -> RECEIPT_APPROVAL -> RESULT_INPUT
            test.moveToNextStage();  // REQUEST -> RECEIPT
            test.moveToNextStage();  // RECEIPT -> RECEIPT_APPROVAL
            test.moveToNextStage();  // RECEIPT_APPROVAL -> RESULT_INPUT
            System.out.println("🔧 Stage moved to RESULT_INPUT (no approval required): " + test.getStage());
        }

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
        // stage는 이미 RECEIPT_APPROVAL 상태이므로 변경하지 않음
        // 승인 요청만 생성하고 승인이 완료되면 ApprovalDomainService에서 자동으로 RESULT_INPUT으로 이동
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

    @Override
    @Transactional
    public void inputResult(Long testId, ResultInfo resultInfo) {
        System.out.println("🔧 inputResult started - Test ID: " + testId);

        Test test = testValidator.validateTestExists(testId, testCrudService);
        System.out.println("🔧 Test found - Current stage: " + test.getStage());

        testResultCrudService.insert(resultInfo);
        System.out.println("🔧 ResultInfo inserted - ID: " + resultInfo.getId());

        test.setResultInfoId(resultInfo.getId());
        System.out.println("🔧 Result Info ID set to test: " + test.getResultInfoId());

        // 결재 필요 여부에 따라 단계 분기
        if (resultInfo.isRequiresApproval()) {
            // 결재 필요: RESULT_INPUT -> RESULT_APPROVAL
            test.moveToNextStage();
            System.out.println("🔧 Stage moved to RESULT_APPROVAL (approval required): " + test.getStage());
        } else {
            // 결재 불필요: RESULT_INPUT -> RESULT_APPROVAL -> COMPLETED
            test.moveToNextStage();  // RESULT_INPUT -> RESULT_APPROVAL
            test.moveToNextStage();  // RESULT_APPROVAL -> COMPLETED
            System.out.println("🔧 Stage moved to COMPLETED (no approval required): " + test.getStage());
        }

        testCrudService.updateTest(test);
        System.out.println("🔧 Test updated in DB");
    }

    @Override
    @Transactional
    public void moveToResultApproval(Long testId, ApprovalCreateDto approvalDto) {
        Test test = testValidator.validateTestExists(testId, testCrudService);
        ResultInfo resultInfo = testValidator.validateResultInfoExists(test.getResultInfoId(), testResultCrudService);

        if (resultInfo.isRequiresApproval()) {
            approvalService.createApproval(approvalDto);
        }

        testResultCrudService.update(resultInfo);
        // stage는 이미 RESULT_APPROVAL 상태이므로 변경하지 않음
        // 승인 요청만 생성하고 승인이 완료되면 ApprovalDomainService에서 자동으로 COMPLETED로 이동
    }

    @Override
    @Transactional
    public void moveBackToResultInput(Long testId, Long approvalId, ApprovalSignUpdateDto updateDto) {
        Test test = testValidator.validateTestExists(testId, testCrudService);

        updateApprovalSign(approvalId, updateDto);

        test.moveToPreviousStage();
        testCrudService.updateTest(test);
    }

    @Override
    @Transactional
    public void moveToCompleted(Long testId, Long approvalId, ApprovalSignUpdateDto updateDto) {
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