package com.lims.lims_study.application.test.service;

import com.lims.lims_study.application.test.dto.*;

import java.util.List;

public interface ITestApplicationService {
    TestResponseDto createTest(TestCreateDto dto);
    TestResponseDto updateTest(Long testId, TestUpdateDto dto);
    void deleteTest(Long testId);
    TestResponseDto getTest(Long testId);
    List<TestResponseDto> searchTests(TestSearchDto dto);
    TestResponseDto moveToReceipt(Long testId, ReceiptCreateDto dto);
    TestResponseDto moveBackToRequest(Long testId);
}