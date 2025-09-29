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
}