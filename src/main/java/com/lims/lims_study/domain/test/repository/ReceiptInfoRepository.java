package com.lims.lims_study.domain.test.repository;

import com.lims.lims_study.domain.test.model.ReceiptInfo;
import com.lims.lims_study.global.common.BaseRepository;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReceiptInfoRepository extends BaseRepository<ReceiptInfo, Long> {

}