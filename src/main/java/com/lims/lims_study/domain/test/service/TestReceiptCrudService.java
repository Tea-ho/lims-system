package com.lims.lims_study.domain.test.service;

import com.lims.lims_study.domain.test.model.ReceiptInfo;
import com.lims.lims_study.domain.test.repository.ReceiptInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestReceiptCrudService implements ITestReceiptCrudService{

    private final ReceiptInfoMapper receiptInfoMapper;

    @Override
    @Transactional
    public void insert(ReceiptInfo receiptInfo) {
        receiptInfoMapper.insert(receiptInfo);
    }

    @Override
    @Transactional
    public void update(ReceiptInfo receiptInfo) {
        receiptInfoMapper.update(receiptInfo);
    }

    @Override
    public Optional<ReceiptInfo> findById(Long id) {
        return receiptInfoMapper.findById(id);
    }
}
