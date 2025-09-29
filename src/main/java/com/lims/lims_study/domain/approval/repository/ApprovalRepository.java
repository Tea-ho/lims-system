package com.lims.lims_study.domain.approval.repository;

import com.lims.lims_study.domain.approval.model.Approval;
import com.lims.lims_study.domain.approval.model.ApprovalStatus;
import com.lims.lims_study.global.common.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ApprovalRepository extends BaseRepository<Approval, Long> {
    List<Approval> findByTargetId(Long targetId);

    List<Approval> findByStatus(ApprovalStatus status);

    List<Approval> findPendingApprovals();

    /**
     * 동시성 제어를 위한 버전 기반 업데이트
     * @param approval 업데이트할 승인 엔티티
     * @param expectedVersion 기대하는 버전
     * @return 업데이트된 행 수 (1: 성공, 0: 버전 충돌)
     */
    int updateWithVersion(@Param("approval") Approval approval, @Param("expectedVersion") Long expectedVersion);

    long countPendingApprovals();
}