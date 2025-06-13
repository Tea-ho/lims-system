package com.lims.lims_study.domain.test.repository;

import com.lims.lims_study.domain.test.model.Test;
import com.lims.lims_study.domain.test.model.TestStage;
import com.lims.lims_study.global.common.BaseRepository;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestRepository extends BaseRepository<Test, Long> {
    List<Test> findByStage(TestStage stage);
    List<Test> findBySearchConditions(String title, String username, TestStage stage);
}