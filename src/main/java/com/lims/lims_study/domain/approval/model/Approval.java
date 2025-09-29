package com.lims.lims_study.domain.approval.model;

import java.time.LocalDateTime;

/**
 * 승인 마스터 엔티티
 * - 동시성 문제 해결을 위해 version 필드 추가
 * - 전체 승인 상태 관리
 */
public class Approval {
    private Long id;
    private ApprovalStatus status;
    private Long version;           // 동시성 제어를 위한 버전 필드
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Approval(ApprovalStatus status) {
        this.status = status;
        this.version = 0L;          // 초기 버전
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 승인 상태 업데이트 (버전 증가)
     * @param status 새로운 승인 상태
     */
    public void updateStatus(ApprovalStatus status) {
        this.status = status;
        this.version++;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 동시성 체크를 위한 버전 비교
     * @param expectedVersion 기대하는 버전
     * @return 버전 일치 여부
     */
    public boolean isVersionMatch(Long expectedVersion) {
        return this.version.equals(expectedVersion);
    }

    // Getters
    public Long getId() { return id; }
    public ApprovalStatus getStatus() { return status; }
    public Long getVersion() { return version; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // 테스트를 위한 setter (실제로는 Repository에서 설정)
    public void setId(Long id) { this.id = id; }
    public void setVersion(Long version) { this.version = version; }
}