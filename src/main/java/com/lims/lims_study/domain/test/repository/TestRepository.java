package com.lims.lims_study.domain.test.repository;

import com.lims.lims_study.application.test.dto.RecentTestDto;
import com.lims.lims_study.application.test.dto.TestResponseDto;
import com.lims.lims_study.application.test.dto.TestSearchDto;
import com.lims.lims_study.domain.test.model.Test;
import com.lims.lims_study.domain.test.model.TestStage;
import com.lims.lims_study.global.common.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TestRepository extends BaseRepository<Test, Long> {
    List<Test> findByStage(TestStage stage);
    List<Test> findBySearchConditions(TestSearchDto searchDto);

    // 대시보드용 메서드들
    long countAllTests();
    long countPendingTests();
    long countCompletedTests();
    long countMyTests(@Param("requesterId") Long requesterId);
    long countByStage(@Param("stage") String stage);
    List<RecentTestDto> findRecentTests(@Param("limit") int limit);

    // 추가 통계 메서드
    long countTestsLastMonth();
    long countTestsThisMonth();
    long countCompletedTestsYesterday();
    long countPendingTestsYesterday();
}