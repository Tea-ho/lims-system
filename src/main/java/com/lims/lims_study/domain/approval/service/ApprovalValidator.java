package com.lims.lims_study.domain.approval.service;

import com.lims.lims_study.application.approval.dto.ApprovalCreateDto;
import com.lims.lims_study.application.approval.dto.ApprovalSignUpdateDto;
import com.lims.lims_study.domain.approval.model.Approval;
import com.lims.lims_study.domain.approval.model.ApprovalSign;
import com.lims.lims_study.domain.approval.repository.ApprovalRepository;
import com.lims.lims_study.domain.approval.repository.ApprovalSignRepository;
import com.lims.lims_study.domain.product.repository.ProductRepository;
import com.lims.lims_study.domain.test.repository.TestRepository;
import com.lims.lims_study.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ApprovalValidator {
    private final TestRepository testRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ApprovalRepository approvalRepository;
    private final ApprovalSignRepository approvalSignRepository;

    public void checkRequesterExists(Long requesterId) {
        userRepository.findById(requesterId)
                .orElseThrow(() -> new IllegalArgumentException("Requester not found: " + requesterId));
    }

    public void checkApproverAndTargetExists(List<ApprovalCreateDto.ApprovalSignDto> signs) {
        for (ApprovalCreateDto.ApprovalSignDto signDto : signs) {
            userRepository.findById(signDto.getApproverId())
                    .orElseThrow(() -> new IllegalArgumentException("Approver not found: " + signDto.getApproverId()));

            if (!testRepository.findById(signDto.getTargetId()).isPresent() &&
                    !userRepository.findById(signDto.getTargetId()).isPresent() &&
                    !productRepository.findById(signDto.getTargetId()).isPresent()) {
                throw new IllegalArgumentException("Target not found: " + signDto.getTargetId());
            }
        }
    }
    public Approval findAndVerifyApproval(Long approvalId) {
        return approvalRepository.findById(approvalId)
                .orElseThrow(() -> new IllegalArgumentException("Approval not found: " + approvalId));
    }

    public ApprovalSign findAndVerifyApprovalSign(Long approvalId, Long signId) {
        ApprovalSign sign = approvalSignRepository.findById(signId)
                .orElseThrow(() -> new IllegalArgumentException("Approval sign not found: " + signId));
        if (!sign.getApprovalId().equals(approvalId)) {
            throw new IllegalArgumentException("Approval sign " + signId + " does not belong to approval " + approvalId);
        }
        return sign;
    }

    public void checkStatusForSignUpdate(ApprovalSignUpdateDto dto) {
        if (dto.getStatus() == null) {
            throw new IllegalArgumentException("Status is required");
        }
    }

    public void ensureUniqueSignsForApproval(Long approvalId, List<ApprovalCreateDto.ApprovalSignDto> signs) {
        for (ApprovalCreateDto.ApprovalSignDto sign : signs) {
            approvalSignRepository.findByApprovalIdAndTargetIdAndStage(approvalId, sign.getTargetId(), sign.getStage())
                    .ifPresent(s -> {
                        throw new IllegalArgumentException("Approval sign already exists for target " +
                                sign.getTargetId() + " and stage " + sign.getStage());
                    });
        }
    }
}