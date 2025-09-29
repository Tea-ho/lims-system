package com.lims.lims_study.domain.test.repository;

import com.lims.lims_study.domain.test.model.ResultInfo;
import com.lims.lims_study.global.common.BaseRepository;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResultInfoRepository extends BaseRepository<ResultInfo, Long> {

}