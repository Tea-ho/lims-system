package com.lims.lims_study.domain.test.service;

import com.lims.lims_study.application.approval.dto.ApprovalCreateDto;
import com.lims.lims_study.application.approval.dto.ApprovalResponseDto;
import com.lims.lims_study.application.approval.dto.ApprovalSignUpdateDto;
import com.lims.lims_study.domain.approval.model.ApprovalStatus;
import com.lims.lims_study.domain.approval.service.IApprovalService;
import com.lims.lims_study.domain.test.model.ReceiptInfo;
import com.lims.lims_study.domain.test.model.RequestInfo;
import com.lims.lims_study.domain.test.model.ResultInfo;
import com.lims.lims_study.domain.test.model.TestStage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestStateServiceTest {

    @Mock
    private ITestCrudService testCrudService;

    @Mock
    private ITestReceiptCrudService testReceiptCrudService;

    @Mock
    private ITestResultCrudService testResultCrudService;

    @Mock
    private TestValidator testValidator;

    @Mock
    private IApprovalService approvalService;

    @InjectMocks
    private TestStateService testStateService;

    private com.lims.lims_study.domain.test.model.Test testEntity;
    private ReceiptInfo receiptInfo;
    private ResultInfo resultInfo;

    @BeforeEach
    void setUp() {
        RequestInfo requestInfo = new RequestInfo("테스트 목적", "테스트 방법", true);
        testEntity = new com.lims.lims_study.domain.test.model.Test(1L, 1L, requestInfo);
        testEntity.setId(1L);

        receiptInfo = new ReceiptInfo("LOT-001", true);
        receiptInfo.setId(1L);

        resultInfo = new ResultInfo("합격", "PASS", "단위", true);
        resultInfo.setId(1L);
    }

    @org.junit.jupiter.api.Test
    @DisplayName("접수 단계로 이동 - 승인 필요")
    void moveToReceipt_RequiresApproval() {
        // given
        when(testValidator.validateTestExists(eq(1L), eq(testCrudService)))
                .thenReturn(testEntity);

        // when
        testStateService.moveToReceipt(1L, receiptInfo);

        // then
        verify(testReceiptCrudService).insert(receiptInfo);
        verify(testCrudService).updateTest(testEntity);
        assertEquals("RECEIPT", testEntity.getStage());
    }

    @org.junit.jupiter.api.Test
    @DisplayName("접수 단계로 이동 - 승인 불필요")
    void moveToReceipt_NoApprovalRequired() {
        // given
        receiptInfo.setRequiresApproval(false);
        when(testValidator.validateTestExists(eq(1L), eq(testCrudService)))
                .thenReturn(testEntity);

        // when
        testStateService.moveToReceipt(1L, receiptInfo);

        // then
        verify(testReceiptCrudService).insert(receiptInfo);
        verify(testCrudService).updateTest(testEntity);
        assertEquals("RESULT_INPUT", testEntity.getStage());
    }

    @org.junit.jupiter.api.Test
    @DisplayName("의뢰 단계로 복귀")
    void moveBackToRequest() {
        // given
        testEntity.setStage("RECEIPT");
        when(testValidator.validateTestExists(eq(1L), eq(testCrudService)))
                .thenReturn(testEntity);

        // when
        testStateService.moveBackToRequest(1L);

        // then
        verify(testCrudService).updateTest(testEntity);
        assertEquals("REQUEST", testEntity.getStage());
    }

    @org.junit.jupiter.api.Test
    @DisplayName("결과 입력 - 승인 필요")
    void inputResult_RequiresApproval() {
        // given
        testEntity.setStage("RESULT_INPUT");
        when(testValidator.validateTestExists(eq(1L), eq(testCrudService)))
                .thenReturn(testEntity);

        // when
        testStateService.inputResult(1L, resultInfo);

        // then
        verify(testResultCrudService).insert(resultInfo);
        verify(testCrudService).updateTest(testEntity);
        assertEquals("RESULT_APPROVAL", testEntity.getStage());
    }

    @org.junit.jupiter.api.Test
    @DisplayName("결과 입력 - 승인 불필요")
    void inputResult_NoApprovalRequired() {
        // given
        testEntity.setStage("RESULT_INPUT");
        resultInfo.setRequiresApproval(false);
        when(testValidator.validateTestExists(eq(1L), eq(testCrudService)))
                .thenReturn(testEntity);

        // when
        testStateService.inputResult(1L, resultInfo);

        // then
        verify(testResultCrudService).insert(resultInfo);
        verify(testCrudService).updateTest(testEntity);
        assertEquals("COMPLETED", testEntity.getStage());
    }

    @org.junit.jupiter.api.Test
    @DisplayName("완료 단계로 이동")
    void moveToCompleted() {
        // given
        testEntity.setStage("RESULT_APPROVAL");
        ApprovalSignUpdateDto updateDto = new ApprovalSignUpdateDto(ApprovalStatus.APPROVED, "승인");

        ApprovalResponseDto.ApprovalSignDto signDto = new ApprovalResponseDto.ApprovalSignDto(
                1L, 1L, 100L, TestStage.RESULT_APPROVAL, ApprovalStatus.PENDING, null, null, null
        );

        ApprovalResponseDto.ApprovalRequestDto requestDto = new ApprovalResponseDto.ApprovalRequestDto(
                1L, "요청자", "승인 요청", null, null
        );

        ApprovalResponseDto approvalDto = new ApprovalResponseDto(
                1L, ApprovalStatus.PENDING, 1L, requestDto, Arrays.asList(signDto), null, null
        );

        when(testValidator.validateTestExists(eq(1L), eq(testCrudService)))
                .thenReturn(testEntity);
        when(approvalService.getApproval(1L))
                .thenReturn(approvalDto);

        // when
        testStateService.moveToCompleted(1L, 1L, updateDto);

        // then
        verify(testCrudService).updateTest(testEntity);
        assertEquals("COMPLETED", testEntity.getStage());
    }

    @org.junit.jupiter.api.Test
    @DisplayName("결과입력 단계로 복귀")
    void moveBackToResultInput() {
        // given
        testEntity.setStage("RESULT_APPROVAL");
        ApprovalSignUpdateDto updateDto = new ApprovalSignUpdateDto(ApprovalStatus.REJECTED, "반려");

        ApprovalResponseDto.ApprovalSignDto signDto = new ApprovalResponseDto.ApprovalSignDto(
                1L, 1L, 100L, TestStage.RESULT_APPROVAL, ApprovalStatus.PENDING, null, null, null
        );

        ApprovalResponseDto.ApprovalRequestDto requestDto = new ApprovalResponseDto.ApprovalRequestDto(
                1L, "요청자", "승인 요청", null, null
        );

        ApprovalResponseDto approvalDto = new ApprovalResponseDto(
                1L, ApprovalStatus.PENDING, 1L, requestDto, Arrays.asList(signDto), null, null
        );

        when(testValidator.validateTestExists(eq(1L), eq(testCrudService)))
                .thenReturn(testEntity);
        when(approvalService.getApproval(1L))
                .thenReturn(approvalDto);

        // when
        testStateService.moveBackToResultInput(1L, 1L, updateDto);

        // then
        verify(testCrudService).updateTest(testEntity);
        assertEquals("RESULT_INPUT", testEntity.getStage());
    }
}
