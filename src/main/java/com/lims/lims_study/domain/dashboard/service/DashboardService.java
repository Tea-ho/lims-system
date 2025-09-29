package com.lims.lims_study.domain.dashboard.service;

import com.lims.lims_study.domain.approval.repository.ApprovalRepository;
import com.lims.lims_study.domain.dashboard.dto.DashboardResponseDto;
import com.lims.lims_study.domain.dashboard.dto.DashboardStatsDto;
import com.lims.lims_study.domain.product.repository.ProductRepository;
import com.lims.lims_study.application.test.dto.RecentTestDto;
import com.lims.lims_study.application.test.dto.TestResponseDto;
import com.lims.lims_study.domain.test.repository.TestRepository;
import com.lims.lims_study.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DashboardService {

    private final TestRepository testRepository;
    private final ProductRepository productRepository;
    private final ApprovalRepository approvalRepository;
    private final UserRepository userRepository;

    public DashboardResponseDto getDashboardData() {
        log.info("Fetching dashboard data");

        // 현재 사용자 ID (임시로 1L 사용)
        Long currentUserId = 1L;

        // 통계 데이터 조회
        DashboardStatsDto stats = getDashboardStats(currentUserId);

        // 단계별 카운트 조회
        Map<String, Long> stageCounts = getStageCountsFromDb();

        // 최근 시험 데이터 조회 (최대 5개)
        List<RecentTestDto> recentTests = testRepository.findRecentTests(5);

        return DashboardResponseDto.builder()
                .stats(stats)
                .stageCounts(stageCounts)
                .recentTests(recentTests)
                .build();
    }

    private DashboardStatsDto getDashboardStats(Long currentUserId) {
        // 전체 시험 수
        long totalTests = testRepository.countAllTests();

        // 대기 중인 시험 수 (REQUEST, RECEIPT, RECEIPT_APPROVAL, RESULT_INPUT 단계)
        long pendingTests = testRepository.countPendingTests();

        // 완료된 시험 수
        long completedTests = testRepository.countCompletedTests();

        // 내 시험 수 (현재 사용자가 의뢰한 시험)
        long myTests = 0;
        if (currentUserId != null) {
            myTests = testRepository.countMyTests(currentUserId);
        }

        // 전체 제품 수
        long totalProducts = productRepository.countAllProducts();

        // 대기 중인 승인 수
        long pendingApprovals = approvalRepository.countPendingApprovals();

        return DashboardStatsDto.builder()
                .totalTests(totalTests)
                .pendingTests(pendingTests)
                .completedTests(completedTests)
                .myTests(myTests)
                .totalProducts(totalProducts)
                .pendingApprovals(pendingApprovals)
                .build();
    }

    private Map<String, Long> getStageCountsFromDb() {
        Map<String, Long> stageCounts = new HashMap<>();

        // 각 단계별 시험 수 조회
        stageCounts.put("REQUEST", testRepository.countByStage("REQUEST"));
        stageCounts.put("RECEIPT", testRepository.countByStage("RECEIPT"));
        stageCounts.put("RECEIPT_APPROVAL", testRepository.countByStage("RECEIPT_APPROVAL"));
        stageCounts.put("RESULT_INPUT", testRepository.countByStage("RESULT_INPUT"));
        stageCounts.put("COMPLETED", testRepository.countByStage("COMPLETED"));

        return stageCounts;
    }
}