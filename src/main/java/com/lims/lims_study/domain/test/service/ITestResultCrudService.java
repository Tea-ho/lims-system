package com.lims.lims_study.domain.test.service;

import com.lims.lims_study.domain.test.model.ResultInfo;

import java.util.Optional;

public interface ITestResultCrudService {
    void insert(ResultInfo resultInfo);
    void update(ResultInfo resultInfo);
    void delete(Long id);
    Optional<ResultInfo> findById(Long id);
}
