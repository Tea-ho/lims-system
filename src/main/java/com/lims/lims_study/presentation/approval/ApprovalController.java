package com.lims.lims_study.presentation.approval;

import com.lims.lims_study.application.approval.dto.ApprovalCreateDto;
import com.lims.lims_study.application.approval.dto.ApprovalResponseDto;
import com.lims.lims_study.application.approval.dto.ApprovalSignUpdateDto;
import com.lims.lims_study.application.approval.service.IApprovalApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/approvals")
@RequiredArgsConstructor
public class ApprovalController {

    private final IApprovalApplicationService approvalService;

    @PostMapping
    public ResponseEntity<ApprovalResponseDto> createApproval(@RequestBody ApprovalCreateDto dto) {
        ApprovalResponseDto response = approvalService.createApproval(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{approvalId}/signs/{signId}")
    public ResponseEntity<ApprovalResponseDto> updateApprovalSign(
            @PathVariable Long approvalId,
            @PathVariable Long signId,
            @Valid @RequestBody ApprovalSignUpdateDto dto) {
        ApprovalResponseDto response = approvalService.updateApprovalSign(approvalId, signId, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{approvalId}")
    public ResponseEntity<Void> deleteApproval(@PathVariable Long approvalId) {
        approvalService.deleteApproval(approvalId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{approvalId}")
    public ResponseEntity<ApprovalResponseDto> getApproval(@PathVariable Long approvalId) {
        ApprovalResponseDto response = approvalService.getApproval(approvalId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/target/{targetId}")
    public ResponseEntity<List<ApprovalResponseDto>> getApprovalsByTarget(@PathVariable Long targetId) {
        List<ApprovalResponseDto> responses = approvalService.getApprovalsByTarget(targetId);
        return ResponseEntity.ok(responses);
    }
}