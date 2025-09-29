package com.lims.lims_study.domain.dashboard.dto;

import com.lims.lims_study.application.test.dto.RecentTestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponseDto {
    private DashboardStatsDto stats;
    private Map<String, Long> stageCounts;
    private List<RecentTestDto> recentTests;
}