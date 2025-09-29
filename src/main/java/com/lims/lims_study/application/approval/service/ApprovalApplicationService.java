package com.lims.lims_study.application.approval.service;

import com.lims.lims_study.application.approval.dto.ApprovalCreateDto;
import com.lims.lims_study.application.approval.dto.ApprovalResponseDto;
import com.lims.lims_study.application.approval.dto.ApprovalSignUpdateDto;
import com.lims.lims_study.domain.approval.model.ApprovalStatus;
import com.lims.lims_study.domain.approval.service.IApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalApplicationService implements IApprovalApplicationService {
    private final IApprovalService approvalService;

    @Override
    @Transactional
    public ApprovalResponseDto createApproval(ApprovalCreateDto dto) {
        return approvalService.createApproval(dto);
    }

    @Override
    @Transactional
    public ApprovalResponseDto updateApprovalSign(Long approvalId, Long signId, ApprovalSignUpdateDto dto) {
        return approvalService.updateApprovalSign(approvalId, signId, dto);
    }

    @Override
    @Transactional
    public void deleteApproval(Long approvalId) {
        approvalService.deleteApproval(approvalId);
    }

    @Override
    public ApprovalResponseDto getApproval(Long approvalId) {
        return approvalService.getApproval(approvalId);
    }

    @Override
    public List<ApprovalResponseDto> getApprovalsByTarget(Long targetId) {
        return approvalService.getApprovalsByTarget(targetId);
    }

    @Override
    public List<ApprovalResponseDto> getPendingApprovals(ApprovalStatus status, String search) {
        return approvalService.getPendingApprovals(status, search);
    }
}