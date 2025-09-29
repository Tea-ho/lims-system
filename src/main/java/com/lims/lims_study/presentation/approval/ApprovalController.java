package com.lims.lims_study.presentation.approval;

import com.lims.lims_study.application.approval.dto.ApprovalCreateDto;
import com.lims.lims_study.application.approval.dto.ApprovalResponseDto;
import com.lims.lims_study.application.approval.dto.ApprovalSignUpdateDto;
import com.lims.lims_study.application.approval.service.IApprovalApplicationService;
import com.lims.lims_study.domain.approval.model.ApprovalStatus;
import com.lims.lims_study.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<ApprovalResponseDto>>> getPendingApprovals(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) ApprovalStatus status,
            @RequestParam(required = false) String search) {

        List<ApprovalResponseDto> approvals = approvalService.getPendingApprovals(status, search);
        ApiResponse<List<ApprovalResponseDto>> response = ApiResponse.success(approvals);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{approvalId}/process")
    public ResponseEntity<ApiResponse<ApprovalResponseDto>> processApproval(
            @PathVariable Long approvalId,
            @RequestBody Map<String, String> request) {

        ApprovalStatus status = ApprovalStatus.valueOf(request.get("status"));
        String comment = request.get("comment");

        // TODO: Implement processApproval method in service
        ApprovalResponseDto response = approvalService.getApproval(approvalId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}