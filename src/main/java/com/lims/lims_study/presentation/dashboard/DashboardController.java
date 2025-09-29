package com.lims.lims_study.presentation.dashboard;

import com.lims.lims_study.domain.dashboard.service.DashboardService;
import com.lims.lims_study.domain.dashboard.dto.DashboardResponseDto;
import com.lims.lims_study.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponseDto>> getDashboardData() {
        DashboardResponseDto dashboardData = dashboardService.getDashboardData();
        return ResponseEntity.ok(ApiResponse.success(dashboardData, "Dashboard data retrieved successfully"));
    }
}
