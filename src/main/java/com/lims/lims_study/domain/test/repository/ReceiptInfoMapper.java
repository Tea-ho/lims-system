package com.lims.lims_study.domain.test.repository;

import com.lims.lims_study.domain.test.model.ReceiptInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface ReceiptInfoMapper {

    void insert(ReceiptInfo receiptInfo);

    void update(ReceiptInfo receiptInfo);

    Optional<ReceiptInfo> findById(Long id);
}