package com.lims.lims_study.application.approval.service;

import com.lims.lims_study.application.approval.dto.ApprovalCreateDto;
import com.lims.lims_study.application.approval.dto.ApprovalResponseDto;
import com.lims.lims_study.application.approval.dto.ApprovalSignUpdateDto;

import com.lims.lims_study.domain.approval.model.ApprovalStatus;

import java.util.List;

public interface IApprovalApplicationService {
    ApprovalResponseDto createApproval(ApprovalCreateDto dto);
    ApprovalResponseDto updateApprovalSign(Long approvalId, Long signId, ApprovalSignUpdateDto dto);
    void deleteApproval(Long approvalId);
    ApprovalResponseDto getApproval(Long approvalId);
    List<ApprovalResponseDto> getApprovalsByTarget(Long testId);
    List<ApprovalResponseDto> getPendingApprovals(ApprovalStatus status, String search);
}
