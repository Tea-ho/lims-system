package com.lims.lims_study.domain.test.service;

import com.lims.lims_study.domain.test.model.RequestInfo;
import com.lims.lims_study.domain.test.repository.RequestInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestRequestCrudService implements ITestRequestCrudService {
    private final RequestInfoRepository requestInfo;

    @Override
    public void insert(RequestInfo requestInfo) {
        this.requestInfo.insert(requestInfo);
    }

    @Override
    public void update(RequestInfo requestInfo) {
        this.requestInfo.update(requestInfo);
    }

    @Override
    public void delete(Long id) {
        requestInfo.delete(id);
    }

    @Override
    public Optional<RequestInfo> findById(Long id) {
        return requestInfo.findById(id);
    }
}
