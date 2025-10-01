package com.lims.lims_study.application.test.service;

import com.lims.lims_study.application.approval.dto.ApprovalCreateDto;
import com.lims.lims_study.application.approval.dto.ApprovalSignUpdateDto;
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
    TestResponseDto moveToReceiptApproval(Long testId, ApprovalCreateDto approvalDto);
    TestResponseDto moveBackToReceipt(Long testId, Long approvalId, ApprovalSignUpdateDto updateDto);
    TestResponseDto moveToInputResult(Long testId, Long approvalId, ApprovalSignUpdateDto updateDto);
    TestResponseDto inputResult(Long testId, ResultCreateDto dto);
    TestResponseDto moveToResultApproval(Long testId, ApprovalCreateDto approvalDto);
    TestResponseDto moveBackToResultInput(Long testId, Long approvalId, ApprovalSignUpdateDto updateDto);
    TestResponseDto moveToCompleted(Long testId, Long approvalId, ApprovalSignUpdateDto updateDto);

}