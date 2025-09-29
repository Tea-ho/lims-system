package com.lims.lims_study.domain.test.repository;

import com.lims.lims_study.domain.test.model.CompletionInfo;
import com.lims.lims_study.global.common.BaseRepository;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface CompletionInfoRepository extends BaseRepository<CompletionInfo, Long> {

}