package com.lims.lims_study.domain.approval.model;

/**
 * 승인 상태 정의
 * - PENDING: 승인 대기 (초기 상태)
 * - PARTIAL_APPROVED: 부분 승인 (일부 승인자만 승인 완료)
 * - APPROVED: 전체 승인 완료 (모든 승인자가 승인)
 * - REJECTED: 반려 (한 명이라도 반려하면 즉시 반려)
 */
public enum ApprovalStatus {
    PENDING,           // 승인 대기
    PARTIAL_APPROVED,  // 부분 승인
    APPROVED,          // 전체 승인 완료
    REJECTED           // 반려
}