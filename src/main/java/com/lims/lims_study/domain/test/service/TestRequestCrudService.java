package com.lims.lims_study.domain.test.service;

import com.lims.lims_study.domain.test.model.RequestInfo;
import com.lims.lims_study.domain.test.repository.RequestInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestRequestCrudService implements ITestRequestCrudService {
    private final RequestInfoMapper requestInfoMapper;

    @Override
    @Transactional
    public void insert(RequestInfo requestInfo) {
        requestInfoMapper.insert(requestInfo);
    }

    @Override
    @Transactional
    public void update(RequestInfo requestInfo) {
        requestInfoMapper.update(requestInfo);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        requestInfoMapper.delete(id);
    }

    @Override
    public Optional<RequestInfo> findById(Long id) {
        return requestInfoMapper.findById(id);
    }
}
