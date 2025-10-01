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
import com.lims.lims_study.domain.test.service.ITestCrudService;
import com.lims.lims_study.domain.test.model.Test;
import com.lims.lims_study.domain.user.model.User;
import com.lims.lims_study.domain.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApprovalDomainService implements IApprovalService {
    private final ApprovalRepository approvalRepository;
    private final ApprovalRequestRepository approvalRequestRepository;
    private final ApprovalSignRepository approvalSignRepository;
    private final ApprovalValidator approvalValidator;
    private final IUserService userService;
    private final ITestCrudService testCrudService;

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
        
        // 동시성 제어를 위한 재시도 로직 (Optimistic Locking)
        int maxRetries = 3;
        int retryCount = 0;
        
        while (retryCount < maxRetries) {
            try {
                Approval approval = approvalValidator.findAndVerifyApproval(approvalId);
                ApprovalSign sign = approvalValidator.findAndVerifyApprovalSign(approvalId, signId);
                Long currentVersion = approval.getVersion();

                // 승인 서명 업데이트
                sign.update(dto.getStatus(), dto.getComment());
                approvalSignRepository.update(sign);

                // 전체 승인 상태 업데이트
                updateApprovalStatus(approval);
                
                // 버전 기반 업데이트 (동시성 체크)
                int updatedRows = approvalRepository.updateWithVersion(approval, currentVersion);
                
                if (updatedRows == 1) {
                    // 성공: 업데이트된 승인 정보 반환
                    Approval updatedApproval = approvalValidator.findAndVerifyApproval(approvalId);
                    return buildResponseDto(updatedApproval);
                } else {
                    // 버전 충돌: 재시도
                    retryCount++;
                    log.warn("승인 업데이트 동시성 충돌 발생. approvalId: {}, 재시도: {}/{}", 
                            approvalId, retryCount, maxRetries);
                    
                    if (retryCount >= maxRetries) {
                        log.error("승인 업데이트 최대 재시도 초과. approvalId: {}", approvalId);
                        throw new IllegalStateException(
                            "동시성 충돌로 인해 승인 업데이트에 실패했습니다. 다시 시도해주세요.");
                    }
                    
                    // 재시도 전 잠시 대기 (50ms)
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("인터럽트가 발생했습니다.", e);
                    }
                }
            } catch (IllegalStateException e) {
                throw e; // 동시성 오류는 그대로 전파
            } catch (Exception e) {
                throw new RuntimeException("승인 업데이트 중 오류가 발생했습니다.", e);
            }
        }
        
        throw new IllegalStateException("최대 재시도 횟수를 초과했습니다.");
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

    @Override
    public List<ApprovalResponseDto> getPendingApprovals(ApprovalStatus status, String search) {
        List<Approval> approvals;
        if (status != null) {
            approvals = approvalRepository.findByStatus(status);
        } else {
            approvals = approvalRepository.findPendingApprovals();
        }

        return approvals.stream()
                .map(this::buildResponseDto)
                .filter(approval -> search == null || search.isEmpty() ||
                       approval.getRequest().getComment().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * 전체 승인 상태 업데이트 (전원 승인제)
     * - 모든 승인자가 승인해야 전체 승인
     * - 한 명이라도 반려하면 전체 반려
     * - 부분 승인 상태 지원
     */
    private void updateApprovalStatus(Approval approval) {
        List<ApprovalSign> signs = approvalSignRepository.findByApprovalId(approval.getId());

        if (signs.isEmpty()) {
            approval.updateStatus(ApprovalStatus.PENDING);
            return;
        }

        long totalSigns = signs.size();
        long approvedCount = signs.stream()
                .filter(sign -> sign.getStatus() == ApprovalStatus.APPROVED)
                .count();
        long rejectedCount = signs.stream()
                .filter(sign -> sign.getStatus() == ApprovalStatus.REJECTED)
                .count();

        // 한 명이라도 반려 → 전체 반려
        if (rejectedCount > 0) {
            approval.updateStatus(ApprovalStatus.REJECTED);

            // Test를 이전 단계로 되돌림
            if (!signs.isEmpty()) {
                Long targetId = signs.get(0).getTargetId();
                com.lims.lims_study.domain.test.model.TestStage stage = signs.get(0).getStage();

                testCrudService.findById(targetId).ifPresent(test -> {
                    if (stage == com.lims.lims_study.domain.test.model.TestStage.RECEIPT_APPROVAL) {
                        log.info("🔙 Receipt approval rejected, moving test {} back to RECEIPT stage", targetId);
                        test.moveToPreviousStage(); // RECEIPT_APPROVAL -> RECEIPT
                    } else if (stage == com.lims.lims_study.domain.test.model.TestStage.RESULT_APPROVAL) {
                        log.info("🔙 Result approval rejected, moving test {} back to RESULT_INPUT stage", targetId);
                        test.moveToPreviousStage(); // RESULT_APPROVAL -> RESULT_INPUT
                    }
                    testCrudService.updateTest(test);
                });
            }
        }
        // 모든 승인자가 승인 → 전체 승인
        else if (approvedCount == totalSigns) {
            approval.updateStatus(ApprovalStatus.APPROVED);

            // Test를 다음 단계로 이동
            if (!signs.isEmpty()) {
                Long targetId = signs.get(0).getTargetId();
                com.lims.lims_study.domain.test.model.TestStage stage = signs.get(0).getStage();

                testCrudService.findById(targetId).ifPresent(test -> {
                    if (stage == com.lims.lims_study.domain.test.model.TestStage.RECEIPT_APPROVAL) {
                        log.info("✅ Receipt approval approved, moving test {} to RESULT_INPUT", targetId);
                        test.moveToNextStage(); // RECEIPT_APPROVAL -> RESULT_INPUT
                    } else if (stage == com.lims.lims_study.domain.test.model.TestStage.RESULT_APPROVAL) {
                        log.info("✅ Result approval approved, moving test {} to COMPLETED", targetId);
                        test.moveToNextStage(); // RESULT_APPROVAL -> COMPLETED
                    }
                    testCrudService.updateTest(test);
                });
            }
        }
        // 일부만 승인 → 부분 승인
        else if (approvedCount > 0) {
            approval.updateStatus(ApprovalStatus.PARTIAL_APPROVED);
        }
        // 아무도 승인하지 않음 → 대기
        else {
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

        // 요청자 정보 조회
        log.info("🔍 Building response for approval: {}, requesterId: {}", approval.getId(), request.getRequesterId());
        String requesterName = userService.findById(request.getRequesterId())
                .map(user -> {
                    log.info("✅ Found requester: {}", user.getUsername());
                    return user.getUsername();
                })
                .orElseGet(() -> {
                    log.warn("❌ Requester not found for ID: {}", request.getRequesterId());
                    return "알 수 없는 사용자";
                });
        log.info("👤 Final requester name: {}", requesterName);

        return new ApprovalResponseDto(
                approval.getId(),
                approval.getStatus(),
                approval.getVersion(),    // 버전 정보 추가
                new ApprovalResponseDto.ApprovalRequestDto(
                        request.getRequesterId(),
                        requesterName,
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