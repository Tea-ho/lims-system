package com.lims.lims_study.domain.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsDto {
    private long totalTests;
    private long pendingTests;
    private long completedTests;
    private long myTests;
    private long totalProducts;
    private long pendingApprovals;

    // 추가 통계 필드
    private long totalTestsLastMonth;      // 지난달 전체 시험 수
    private double monthlyTestGrowthRate;  // 월별 시험 증가율 (%)
    private long completedTestsYesterday;  // 어제 완료된 시험 수
    private long activeProducts;           // 활성 제품 수
    private long pendingTestsChange;       // 처리 대기 변화량
}