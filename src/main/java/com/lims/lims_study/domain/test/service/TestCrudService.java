package com.lims.lims_study.domain.test.service;

import com.lims.lims_study.application.test.dto.TestSearchDto;
import com.lims.lims_study.application.test.dto.TestUpdateDto;
import com.lims.lims_study.domain.test.model.RequestInfo;
import com.lims.lims_study.domain.test.model.TestStage;
import com.lims.lims_study.domain.test.model.Test;
import com.lims.lims_study.domain.test.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestCrudService implements ITestCrudService {
    private final TestRepository test;
    private final ITestRequestCrudService testRequestCrudService;
    private final TestValidator testValidator;

    @Override
    @Transactional
    public void createTest(Test test, RequestInfo requestInfo) {
        testValidator.validateUserAndProductExists(test);
        testRequestCrudService.insert(requestInfo);
        test.setRequestInfoId(requestInfo.getId());
        this.test.insert(test);
    }

    @Override
    public void updateRequestInfo(Long testId, TestUpdateDto dto) {
        Test test = testValidator.validateTestExists(testId, this);
        if (!TestStage.REQUEST.name().equals(test.getStage())) {
            throw new IllegalStateException("Can only update request info in REQUESTED stage");
        }

        Long requestInfoId = test.getRequestInfoId();
        RequestInfo existing = testRequestCrudService.findById(requestInfoId)
                .orElseThrow(() -> new IllegalArgumentException("RequestInfo not found: " + requestInfoId));

        existing.updateRequestInfo(dto);
        testRequestCrudService.update(existing);
    }

    @Override
    @Transactional
    public void deleteTest(Long testId) {
        Test test = testValidator.validateTestExists(testId, this);
        testRequestCrudService.delete(test.getRequestInfoId());
        this.test.delete(testId);
    }

    @Override
    public Optional<Test> findById(Long testId) {
        return test.findById(testId);
    }

    @Override
    public List<Test> findByStage(TestStage stage) {
        return test.findByStage(stage);
    }

    @Override
    public List<Test> findBySearchConditions(TestSearchDto searchDto) {
        return test.findBySearchConditions(searchDto);
    }

    @Override
    public void updateTest(Test test) {
        this.test.update(test);
    }
}