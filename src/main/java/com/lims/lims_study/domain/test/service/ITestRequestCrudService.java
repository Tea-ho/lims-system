package com.lims.lims_study.domain.test.service;

import com.lims.lims_study.domain.test.model.RequestInfo;

import java.util.Optional;

public interface ITestRequestCrudService {
    void insert(RequestInfo requestInfo);

    void update(RequestInfo requestInfo);

    void delete(Long id);

    Optional<RequestInfo> findById(Long id);
}
