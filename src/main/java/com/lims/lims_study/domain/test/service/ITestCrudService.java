package com.lims.lims_study.domain.test.service;

import com.lims.lims_study.domain.test.model.*;

import java.util.List;
import java.util.Optional;

public interface ITestCrudService {
    void createTest(Test test, RequestInfo requestInfo);
    void updateRequestInfo(Long testId, RequestInfo requestInfo);
    void deleteTest(Long testId);
    Optional<Test> findById(Long testId);
    List<Test> findByStage(TestStage stage);
    List<Test> findBySearchConditions(String title, String username, TestStage stage);
    void updateTest(Test test);
}