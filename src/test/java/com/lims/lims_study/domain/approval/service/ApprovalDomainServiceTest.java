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
import com.lims.lims_study.domain.test.model.TestStage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
@DisplayName("전원 승인제 및 동시성 제어 테스트")
class ApprovalDomainServiceTest {

    @Mock
    private ApprovalRepository approvalRepository;
    
    @Mock
    private ApprovalRequestRepository approvalRequestRepository;
    
    @Mock
    private ApprovalSignRepository approvalSignRepository;
    
    @Mock
    private ApprovalValidator approvalValidator;

    @Mock
    private com.lims.lims_study.domain.user.service.IUserService userService;

    @Mock
    private com.lims.lims_study.domain.test.service.ITestCrudService testCrudService;

    @InjectMocks
    private ApprovalDomainService approvalDomainService;
    
    private Approval approval;
    private ApprovalRequest approvalRequest;
    private List<ApprovalSign> approvalSigns;
    
    @BeforeEach
    void setUp() {
        approval = new Approval(ApprovalStatus.PENDING);
        approval.setId(1L);
        approval.setVersion(0L);

        approvalRequest = new ApprovalRequest(1L, 100L, "테스트 승인 요청");

        // 3명의 승인자 설정
        approvalSigns = Arrays.asList(
            new ApprovalSign(1L, 200L, 1000L, TestStage.REQUEST, ApprovalStatus.PENDING, null),
            new ApprovalSign(1L, 201L, 1000L, TestStage.REQUEST, ApprovalStatus.PENDING, null),
            new ApprovalSign(1L, 202L, 1000L, TestStage.REQUEST, ApprovalStatus.PENDING, null)
        );

        // userService mock 설정 (requester 정보 조회)
        com.lims.lims_study.domain.user.model.User mockUser = mock(com.lims.lims_study.domain.user.model.User.class);
        when(mockUser.getUsername()).thenReturn("테스트사용자");
        when(userService.findById(100L)).thenReturn(Optional.of(mockUser));
    }
    
    @Test
    @DisplayName("전원 승인제: 모든 승인자가 승인해야 전체 승인")
    void shouldRequireAllApproversForFullApproval() {
        // Given: 3명 중 2명만 승인
        approvalSigns.get(0).update(ApprovalStatus.APPROVED, "승인");
        approvalSigns.get(1).update(ApprovalStatus.APPROVED, "승인");
        // approvalSigns.get(2)는 여전히 PENDING

        when(approvalValidator.findAndVerifyApproval(1L)).thenReturn(approval);
        when(approvalValidator.findAndVerifyApprovalSign(1L, 1L)).thenReturn(approvalSigns.get(0));
        when(approvalSignRepository.findByApprovalId(1L)).thenReturn(approvalSigns);
        when(approvalRepository.updateWithVersion(any(Approval.class), eq(0L))).thenReturn(1);
        when(approvalRequestRepository.findByApprovalId(1L)).thenReturn(Optional.of(approvalRequest));

        ApprovalSignUpdateDto dto = new ApprovalSignUpdateDto();
        dto.setStatus(ApprovalStatus.APPROVED);
        dto.setComment("승인");

        // When
        ApprovalResponseDto result = approvalDomainService.updateApprovalSign(1L, 1L, dto);

        // Then: 부분 승인 상태가 되어야 함
        assertThat(result).isNotNull();
        assertThat(approval.getStatus()).isEqualTo(ApprovalStatus.PARTIAL_APPROVED);
    }
    
    @Test
    @DisplayName("전원 승인제: 모든 승인자가 승인하면 전체 승인")
    void shouldApproveWhenAllApproversApprove() {
        // Given: 모든 승인자가 승인
        approvalSigns.forEach(sign -> sign.update(ApprovalStatus.APPROVED, "승인"));

        when(approvalValidator.findAndVerifyApproval(1L)).thenReturn(approval);
        when(approvalValidator.findAndVerifyApprovalSign(1L, 1L)).thenReturn(approvalSigns.get(0));
        when(approvalSignRepository.findByApprovalId(1L)).thenReturn(approvalSigns);
        when(approvalRepository.updateWithVersion(any(Approval.class), eq(0L))).thenReturn(1);
        when(approvalRequestRepository.findByApprovalId(1L)).thenReturn(Optional.of(approvalRequest));

        ApprovalSignUpdateDto dto = new ApprovalSignUpdateDto();
        dto.setStatus(ApprovalStatus.APPROVED);
        dto.setComment("승인");

        // When
        ApprovalResponseDto result = approvalDomainService.updateApprovalSign(1L, 1L, dto);

        // Then: 전체 승인 상태가 되어야 함
        assertThat(result).isNotNull();
        assertThat(approval.getStatus()).isEqualTo(ApprovalStatus.APPROVED);
    }
    
    @Test
    @DisplayName("즉시 반려: 한 명이라도 반려하면 전체 반려")
    void shouldRejectImmediatelyWhenOneRejects() {
        // Given: 1명 승인, 1명 반려, 1명 대기
        approvalSigns.get(0).update(ApprovalStatus.APPROVED, "승인");
        approvalSigns.get(1).update(ApprovalStatus.REJECTED, "반려");
        // approvalSigns.get(2)는 여전히 PENDING

        when(approvalValidator.findAndVerifyApproval(1L)).thenReturn(approval);
        when(approvalValidator.findAndVerifyApprovalSign(1L, 2L)).thenReturn(approvalSigns.get(1));
        when(approvalSignRepository.findByApprovalId(1L)).thenReturn(approvalSigns);
        when(approvalRepository.updateWithVersion(any(Approval.class), eq(0L))).thenReturn(1);
        when(approvalRequestRepository.findByApprovalId(1L)).thenReturn(Optional.of(approvalRequest));

        ApprovalSignUpdateDto dto = new ApprovalSignUpdateDto();
        dto.setStatus(ApprovalStatus.REJECTED);
        dto.setComment("반려");

        // When
        ApprovalResponseDto result = approvalDomainService.updateApprovalSign(1L, 2L, dto);

        // Then: 전체 반려 상태가 되어야 함
        assertThat(result).isNotNull();
        assertThat(approval.getStatus()).isEqualTo(ApprovalStatus.REJECTED);
    }
    
    @Test
    @DisplayName("동시성 제어: 버전 충돌 시 재시도")
    void shouldRetryOnVersionConflict() {
        // Given: 첫 번째 시도에서 버전 충돌, 두 번째 시도에서 성공
        when(approvalValidator.findAndVerifyApproval(1L)).thenReturn(approval);
        when(approvalValidator.findAndVerifyApprovalSign(1L, 1L)).thenReturn(approvalSigns.get(0));
        when(approvalSignRepository.findByApprovalId(1L)).thenReturn(approvalSigns);
        when(approvalRepository.updateWithVersion(any(Approval.class), anyLong()))
            .thenReturn(0)  // 첫 번째: 버전 충돌
            .thenReturn(1); // 두 번째: 성공
        when(approvalRequestRepository.findByApprovalId(1L)).thenReturn(Optional.of(approvalRequest));

        ApprovalSignUpdateDto dto = new ApprovalSignUpdateDto();
        dto.setStatus(ApprovalStatus.APPROVED);
        dto.setComment("승인");

        // When
        ApprovalResponseDto result = approvalDomainService.updateApprovalSign(1L, 1L, dto);

        // Then: 재시도 후 성공
        assertThat(result).isNotNull();
        verify(approvalRepository, times(2)).updateWithVersion(any(Approval.class), anyLong());
    }
    
    @Test
    @DisplayName("동시성 제어: 최대 재시도 초과 시 예외 발생")
    void shouldThrowExceptionWhenMaxRetriesExceeded() {
        // Given: 모든 시도에서 버전 충돌
        when(approvalValidator.findAndVerifyApproval(1L)).thenReturn(approval);
        when(approvalValidator.findAndVerifyApprovalSign(1L, 1L)).thenReturn(approvalSigns.get(0));
        when(approvalSignRepository.findByApprovalId(1L)).thenReturn(approvalSigns);
        when(approvalRepository.updateWithVersion(any(Approval.class), anyLong()))
            .thenReturn(0); // 항상 버전 충돌
        
        ApprovalSignUpdateDto dto = new ApprovalSignUpdateDto();
        dto.setStatus(ApprovalStatus.APPROVED);
        dto.setComment("승인");
        
        // When & Then: 최대 재시도 후 예외 발생
        assertThatThrownBy(() -> 
            approvalDomainService.updateApprovalSign(1L, 1L, dto)
        )
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("동시성 충돌로 인해 승인 업데이트에 실패했습니다");
        
        // 3번 재시도 확인
        verify(approvalRepository, times(3)).updateWithVersion(any(Approval.class), anyLong());
    }
    
    @Test
    @DisplayName("승인 상태 계산: 빈 승인 목록일 때 PENDING")
    void shouldBePendingWhenNoSigns() {
        // Given: 빈 승인 목록
        when(approvalValidator.findAndVerifyApproval(1L)).thenReturn(approval);
        when(approvalValidator.findAndVerifyApprovalSign(1L, 1L)).thenReturn(approvalSigns.get(0));
        when(approvalSignRepository.findByApprovalId(1L)).thenReturn(Arrays.asList());
        when(approvalRepository.updateWithVersion(any(Approval.class), eq(0L))).thenReturn(1);
        when(approvalRequestRepository.findByApprovalId(1L)).thenReturn(Optional.of(approvalRequest));

        ApprovalSignUpdateDto dto = new ApprovalSignUpdateDto();
        dto.setStatus(ApprovalStatus.APPROVED);
        dto.setComment("승인");

        // When
        ApprovalResponseDto result = approvalDomainService.updateApprovalSign(1L, 1L, dto);

        // Then: PENDING 상태 유지
        assertThat(result).isNotNull();
        assertThat(approval.getStatus()).isEqualTo(ApprovalStatus.PENDING);
    }
    
    @Test
    @DisplayName("버전 필드: 상태 업데이트 시 버전 증가")
    void shouldIncrementVersionOnStatusUpdate() {
        // Given
        Long initialVersion = approval.getVersion();
        
        // When
        approval.updateStatus(ApprovalStatus.APPROVED);
        
        // Then
        assertThat(approval.getVersion()).isEqualTo(initialVersion + 1);
        assertThat(approval.getStatus()).isEqualTo(ApprovalStatus.APPROVED);
    }
    
    @Test
    @DisplayName("버전 매칭: 버전 일치 확인")
    void shouldCheckVersionMatch() {
        // Given
        approval.setVersion(5L);
        
        // When & Then
        assertThat(approval.isVersionMatch(5L)).isTrue();
        assertThat(approval.isVersionMatch(4L)).isFalse();
        assertThat(approval.isVersionMatch(6L)).isFalse();
    }
}
