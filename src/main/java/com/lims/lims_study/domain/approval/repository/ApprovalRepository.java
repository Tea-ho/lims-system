package com.lims.lims_study.domain.approval.repository;

import com.lims.lims_study.domain.approval.model.Approval;
import com.lims.lims_study.global.common.BaseRepository;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ApprovalRepository extends BaseRepository<Approval, Long> {
    List<Approval> findByTargetId(Long targetId);
}