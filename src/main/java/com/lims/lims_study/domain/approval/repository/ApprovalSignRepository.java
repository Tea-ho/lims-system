package com.lims.lims_study.domain.approval.repository;

import com.lims.lims_study.domain.approval.model.ApprovalSign;
import com.lims.lims_study.domain.test.model.TestStage;
import com.lims.lims_study.global.common.BaseRepository;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;
@Mapper
public interface ApprovalSignRepository extends BaseRepository<ApprovalSign, Long> {
    void deleteByApprovalId(Long approvalId);
    Optional<ApprovalSign> findById(Long id);
    List<ApprovalSign> findByApprovalId(Long approvalId);
    Optional<ApprovalSign> findByApprovalIdAndTargetIdAndStage(Long approvalId, Long targetId, TestStage stage);
}