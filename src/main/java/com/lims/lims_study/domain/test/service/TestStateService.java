package com.lims.lims_study.domain.test.service;

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

    @Override
    @Transactional
    public void moveToReceipt(Long testId, ReceiptInfo receiptInfo) {
        Test test = testValidator.validateTestExists(testId, testCrudService);
        testReceiptCrudService.insert(receiptInfo);
        test.setReceiptInfoId(receiptInfo.getId());
        test.moveToNextStage();
        testCrudService.updateTest(test);
    }

    @Override
    @Transactional
    public void moveBackToRequest(Long testId) {
        Test test = testValidator.validateTestExists(testId, testCrudService);
        test.moveToPreviousStage();
        testCrudService.updateTest(test);
    }
}