package com.lims.lims_study.presentation.test;

import com.lims.lims_study.application.approval.dto.ApprovalCreateDto;
import com.lims.lims_study.application.approval.dto.ApprovalSignUpdateDto;
import com.lims.lims_study.application.test.dto.*;
import com.lims.lims_study.application.test.service.ITestApplicationService;
import com.lims.lims_study.application.test.service.TestApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
public class TestController {

    private final ITestApplicationService testApplicationService;

    // 시험 의뢰 생성
    @PostMapping
    public ResponseEntity<TestResponseDto> createTest(@RequestBody TestCreateDto dto) {
        TestResponseDto response = testApplicationService.createTest(dto);
        return ResponseEntity.ok(response);
    }

    // 의뢰 정보 수정
    @PutMapping("/{testId}")
    public ResponseEntity<TestResponseDto> updateTest(@PathVariable Long testId, @RequestBody TestUpdateDto dto) {
        TestResponseDto response = testApplicationService.updateTest(testId, dto);
        return ResponseEntity.ok(response);
    }

    // 의뢰 삭제
    @DeleteMapping("/{testId}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long testId) {
        testApplicationService.deleteTest(testId);
        return ResponseEntity.noContent().build();
    }

    // 시험 조회 (단건)
    @GetMapping("/{testId}")
    public ResponseEntity<TestResponseDto> getTest(@PathVariable Long testId) {
        TestResponseDto response = testApplicationService.getTest(testId);
        return ResponseEntity.ok(response);
    }

    // 시험 검색
    @GetMapping("/search")
    public ResponseEntity<List<TestResponseDto>> searchTests(@ModelAttribute TestSearchDto dto) {
        List<TestResponseDto> response = testApplicationService.searchTests(dto);
        return ResponseEntity.ok(response);
    }

    // 의뢰 -> 접수
    @PostMapping("/{testId}/receipt")
    public ResponseEntity<TestResponseDto> moveToReceipt(@PathVariable Long testId, @RequestBody ReceiptCreateDto dto) {
        TestResponseDto response = testApplicationService.moveToReceipt(testId, dto);
        return ResponseEntity.ok(response);
    }

    // 접수 -> 의뢰
    @PostMapping("/{testId}/back-to-request")
    public ResponseEntity<TestResponseDto> moveBackToRequest(@PathVariable Long testId) {
        TestResponseDto response = testApplicationService.moveBackToRequest(testId);
        return ResponseEntity.ok(response);
    }

    // 접수 -> 접수 결재
    @PostMapping("/{testId}/receipt-approval")
    public ResponseEntity<TestResponseDto> moveToReceiptApproval(@PathVariable Long testId, @RequestBody ApprovalCreateDto approvalDto) {
        TestResponseDto response = testApplicationService.moveToReceiptApproval(testId, approvalDto);
        return ResponseEntity.ok(response);
    }

    // 접수 결재 -> 접수
    @PostMapping("/{testId}/back-to-receipt")
    public ResponseEntity<TestResponseDto> moveBackToReceipt(@PathVariable Long testId, @RequestParam Long approvalId, @RequestBody ApprovalSignUpdateDto updateDto) {
        TestResponseDto response = testApplicationService.moveBackToReceipt(testId, approvalId, updateDto);
        return ResponseEntity.ok(response);
    }
    
    // 접수 결재 -> 결과 입력
    @PostMapping("/{testId}/input-result")
    public ResponseEntity<TestResponseDto> moveToInputResult(@PathVariable Long testId, @RequestParam Long approvalId, @RequestBody ApprovalSignUpdateDto updateDto) {
        TestResponseDto response = testApplicationService.moveToInputResult(testId, approvalId, updateDto);
        return ResponseEntity.ok(response);
    }
}