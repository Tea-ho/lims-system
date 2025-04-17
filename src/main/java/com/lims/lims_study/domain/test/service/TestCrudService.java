package com.lims.lims_study.domain.test.service;

import com.lims.lims_study.domain.test.model.RequestInfo;
import com.lims.lims_study.domain.test.model.Test;
import com.lims.lims_study.domain.test.model.TestStage;
import com.lims.lims_study.domain.test.repository.TestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestCrudService implements ITestCrudService {
    private final TestMapper testMapper;
    private final ITestRequestCrudService testRequestCrudService;
    private final TestValidator testValidator;

    @Override
    @Transactional
    public void createTest(Test test, RequestInfo requestInfo) {
        testValidator.validateCreate(test);
        testRequestCrudService.insert(requestInfo);
        test.setRequestInfoId(requestInfo.getId());
        testMapper.insert(test);
    }

    @Override
    @Transactional
    public void updateRequestInfo(Long testId, RequestInfo requestInfo) {
        Test test = testValidator.validateTestExists(testId, this);
        if (!TestStage.REQUEST.name().equals(test.getStage())) {
            throw new IllegalStateException("Can only update request info in REQUESTED stage");
        }
        requestInfo.setId(test.getRequestInfoId());
        testRequestCrudService.update(requestInfo);
    }

    @Override
    @Transactional
    public void deleteTest(Long testId) {
        Test test = testValidator.validateTestExists(testId, this);
        testRequestCrudService.delete(test.getRequestInfoId());
        testMapper.delete(testId);
    }

    @Override
    public Optional<Test> findById(Long testId) {
        return testMapper.findById(testId);
    }

    @Override
    public List<Test> findByStage(TestStage stage) {
        return testMapper.findByStage(stage);
    }

    @Override
    public List<Test> findBySearchConditions(String title, String username, TestStage stage) {
        return testMapper.findBySearchConditions(title, username, stage);
    }

    @Override
    @Transactional
    public void updateTest(Test test) {
        testMapper.update(test);
    }
}