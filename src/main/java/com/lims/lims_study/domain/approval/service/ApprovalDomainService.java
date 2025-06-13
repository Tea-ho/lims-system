package com.lims.lims_study.domain.approval.service;

import com.lims.lims_study.application.approval.dto.ApprovalCreateDto;
import com.lims.lims_study.application.approval.dto.ApprovalResponseDto;
import com.lims.lims_study.application.approval.dto.ApprovalSignUpdateDto;
import com.lims.lims_study.domain.approval.model.Approval;
import com.lims.lims_study.domain.approval.model.ApprovalRequest;
import com.lims.lims_study.domain.approval.model.ApprovalSign;
import com.lims.lims_study.domain.approval.model.ApprovalStatus;
import com.lims.lims_study.domain.approval.repository.ApprovalRepository;
import com.lims.lims_study.domain.approval.repository.ApprovalRequestRepository;
import com.lims.lims_study.domain.approval.repository.ApprovalSignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ApprovalDomainService implements IApprovalService {
    private final ApprovalRepository approvalRepository;
    private final ApprovalRequestRepository approvalRequestRepository;
    private final ApprovalSignRepository approvalSignRepository;
    private final ApprovalValidator approvalValidator;

    @Override
    @Transactional
    public ApprovalResponseDto createApproval(ApprovalCreateDto dto) {
        approvalValidator.checkRequesterExists(dto.getRequesterId());
        approvalValidator.checkApproverAndTargetExists(dto.getSigns());

        Approval approval = new Approval(ApprovalStatus.PENDING);
        approvalRepository.insert(approval);

        ApprovalRequest request = new ApprovalRequest(approval.getId(), dto.getRequesterId(), dto.getComment());
        approvalRequestRepository.insert(request);

        insertApprovalSign(approval.getId(), dto.getSigns());

        return buildResponseDto(approval);
    }

    @Override
    @Transactional
    public ApprovalResponseDto updateApprovalSign(Long approvalId, Long signId, ApprovalSignUpdateDto dto) {
        approvalValidator.checkStatusForSignUpdate(dto);
        Approval approval = approvalValidator.findAndVerifyApproval(approvalId);
        ApprovalSign sign = approvalValidator.findAndVerifyApprovalSign(approvalId, signId);

        sign.update(dto.getStatus(), dto.getComment());
        approvalSignRepository.update(sign);

        updateApprovalStatus(approval);
        approvalRepository.update(approval);

        return buildResponseDto(approval);
    }

    @Override
    @Transactional
    public void deleteApproval(Long approvalId) {
        approvalValidator.findAndVerifyApproval(approvalId);
        approvalSignRepository.deleteByApprovalId(approvalId);
        approvalRequestRepository.deleteByApprovalId(approvalId);
        approvalRepository.delete(approvalId);
    }

    @Override
    public ApprovalResponseDto getApproval(Long approvalId) {
        Approval approval = approvalValidator.findAndVerifyApproval(approvalId);
        return buildResponseDto(approval);
    }

    @Override
    public List<ApprovalResponseDto> getApprovalsByTarget(Long targetId) {
        List<Approval> approvals = approvalRepository.findByTargetId(targetId);
        return approvals.stream()
                .map(this::buildResponseDto)
                .collect(Collectors.toList());
    }

    private void updateApprovalStatus(Approval approval) {
        List<ApprovalSign> signs = approvalSignRepository.findByApprovalId(approval.getId());
        boolean hasApproved = signs.stream().anyMatch(sign -> sign.getStatus() == ApprovalStatus.APPROVED);
        boolean hasRejected = signs.stream().anyMatch(sign -> sign.getStatus() == ApprovalStatus.REJECTED);

        if (hasRejected) {
            approval.updateStatus(ApprovalStatus.REJECTED);
        } else if (hasApproved) {
            approval.updateStatus(ApprovalStatus.APPROVED);
        } else {
            approval.updateStatus(ApprovalStatus.PENDING);
        }
    }

    private void insertApprovalSign(Long approvalId, List<ApprovalCreateDto.ApprovalSignDto> signs){
        for (ApprovalCreateDto.ApprovalSignDto signDto : signs) {
            ApprovalSign sign = new ApprovalSign(
                    approvalId,
                    signDto.getApproverId(),
                    signDto.getTargetId(),
                    signDto.getStage(),
                    ApprovalStatus.PENDING,
                    null
            );
            approvalSignRepository.insert(sign);
        }
    }

    private ApprovalResponseDto buildResponseDto(Approval approval) {
        ApprovalRequest request = approvalRequestRepository.findByApprovalId(approval.getId())
                .orElseThrow(() -> new IllegalArgumentException("Request not found for approval: " + approval.getId()));
        List<ApprovalSign> signs = approvalSignRepository.findByApprovalId(approval.getId());

        return new ApprovalResponseDto(
                approval.getId(),
                approval.getStatus(),
                new ApprovalResponseDto.ApprovalRequestDto(
                        request.getRequesterId(),
                        request.getComment(),
                        request.getCreatedAt().toString(),
                        request.getUpdatedAt().toString()
                ),
                signs.stream().map(sign -> new ApprovalResponseDto.ApprovalSignDto(
                        sign.getId(),
                        sign.getApproverId(),
                        sign.getTargetId(),
                        sign.getStage(),
                        sign.getStatus(),
                        sign.getComment(),
                        sign.getCreatedAt().toString(),
                        sign.getUpdatedAt().toString()
                )).collect(Collectors.toList()),
                approval.getCreatedAt().toString(),
                approval.getUpdatedAt().toString()
        );
    }
}