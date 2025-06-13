package com.lims.lims_study.domain.test.repository;

import com.lims.lims_study.domain.test.model.RequestInfo;
import com.lims.lims_study.global.common.BaseRepository;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface RequestInfoRepository extends BaseRepository<RequestInfo, Long> {

}