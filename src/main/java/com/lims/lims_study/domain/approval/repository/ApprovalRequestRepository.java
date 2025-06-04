package com.lims.lims_study.domain.approval.repository;

import com.lims.lims_study.domain.approval.model.ApprovalRequest;
import com.lims.lims_study.global.common.BaseRepository;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface ApprovalRequestRepository extends BaseRepository<ApprovalRequest, Long> {
    void deleteByApprovalId(Long approvalId);
    Optional<ApprovalRequest> findByApprovalId(Long approvalId);
}
