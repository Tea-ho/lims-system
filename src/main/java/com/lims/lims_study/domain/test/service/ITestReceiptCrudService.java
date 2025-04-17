package com.lims.lims_study.domain.test.service;

import com.lims.lims_study.domain.test.model.ReceiptInfo;

import java.util.Optional;

public interface ITestReceiptCrudService {
    void insert(ReceiptInfo receiptInfo);

    void update(ReceiptInfo receiptInfo);

    Optional<ReceiptInfo> findById(Long id);
}
