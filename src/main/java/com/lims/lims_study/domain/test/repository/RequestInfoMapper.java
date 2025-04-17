package com.lims.lims_study.domain.test.repository;

import com.lims.lims_study.domain.test.model.RequestInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface RequestInfoMapper {

    void insert(RequestInfo requestInfo);

    void update(RequestInfo requestInfo);

    void delete(Long id);

    Optional<RequestInfo> findById(Long id);
}