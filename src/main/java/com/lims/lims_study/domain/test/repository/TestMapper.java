package com.lims.lims_study.domain.test.repository;

import com.lims.lims_study.domain.test.model.Test;
import com.lims.lims_study.domain.test.model.TestStage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TestMapper {

    void insert(Test test);

    void update(Test test);

    void delete(Long id);

    Optional<Test> findById(Long id);

    List<Test> findByStage(TestStage stage);

    List<Test> findBySearchConditions(String title, String username, TestStage stage);
}