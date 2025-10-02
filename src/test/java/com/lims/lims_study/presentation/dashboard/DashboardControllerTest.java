package com.lims.lims_study.presentation.dashboard;

import com.lims.lims_study.application.test.dto.RecentTestDto;
import com.lims.lims_study.domain.dashboard.dto.DashboardResponseDto;
import com.lims.lims_study.domain.dashboard.dto.DashboardStatsDto;
import com.lims.lims_study.domain.dashboard.service.DashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DashboardController.class,
    excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
    },
    excludeFilters = @org.springframework.context.annotation.ComponentScan.Filter(
        type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE,
        classes = {
            com.lims.lims_study.global.config.SecurityConfig.class,
            com.lims.lims_study.global.config.JwtAuthenticationFilter.class,
            com.lims.lims_study.global.config.JwtProvider.class
        }
    ))
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardService dashboardService;

    private DashboardResponseDto dashboardResponseDto;

    @BeforeEach
    void setUp() {
        DashboardStatsDto stats = DashboardStatsDto.builder()
                .totalTests(100L)
                .pendingTests(20L)
                .completedTests(70L)
                .activeProducts(10L)
                .monthlyTestGrowthRate(12.5)
                .completedTestsYesterday(5L)
                .pendingTestsChange(2L)
                .build();

        RecentTestDto recentTest = new RecentTestDto(
                1L,
                "테스트 제목",
                "테스트 제품",
                "RECEIPT",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        dashboardResponseDto = DashboardResponseDto.builder()
                .stats(stats)
                .recentTests(Arrays.asList(recentTest))
                .stageCounts(Collections.singletonMap("접수", 15L))
                .build();
    }

    @Test
    @DisplayName("대시보드 데이터 조회 테스트")
    void getDashboardData() throws Exception {
        when(dashboardService.getDashboardData())
                .thenReturn(dashboardResponseDto);

        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.stats.totalTests").value(100))
                .andExpect(jsonPath("$.data.stats.pendingTests").value(20))
                .andExpect(jsonPath("$.data.stats.completedTests").value(70))
                .andExpect(jsonPath("$.data.stats.activeProducts").value(10))
                .andExpect(jsonPath("$.data.recentTests[0].title").value("테스트 제목"));
    }
}
