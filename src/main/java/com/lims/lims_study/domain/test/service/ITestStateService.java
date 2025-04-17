package com.lims.lims_study.domain.test.service;

import com.lims.lims_study.domain.test.model.*;

public interface ITestStateService {
    void moveToReceipt(Long testId, ReceiptInfo receiptInfo);
    void moveBackToRequest(Long testId);
}