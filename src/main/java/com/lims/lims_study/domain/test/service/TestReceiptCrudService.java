package com.lims.lims_study.domain.test.service;

import com.lims.lims_study.domain.test.model.ReceiptInfo;
import com.lims.lims_study.domain.test.repository.ReceiptInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestReceiptCrudService implements ITestReceiptCrudService{

    private final ReceiptInfoRepository receiptInfoRepository;

    @Override
    public void insert(ReceiptInfo receiptInfo) {
        this.receiptInfoRepository.insert(receiptInfo);
    }

    @Override
    public void update(ReceiptInfo receiptInfo) {
        this.receiptInfoRepository.update(receiptInfo);
    }

    @Override
    public Optional<ReceiptInfo> findById(Long id) {
        return receiptInfoRepository.findById(id);
    }
}
