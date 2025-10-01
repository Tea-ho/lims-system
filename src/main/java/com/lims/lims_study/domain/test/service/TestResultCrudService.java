package com.lims.lims_study.domain.test.service;

import com.lims.lims_study.domain.test.model.ResultInfo;
import com.lims.lims_study.domain.test.repository.ResultInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestResultCrudService implements ITestResultCrudService {
    private final ResultInfoRepository resultInfoRepository;

    @Override
    @Transactional
    public void insert(ResultInfo resultInfo) {
        resultInfoRepository.insert(resultInfo);
    }

    @Override
    @Transactional
    public void update(ResultInfo resultInfo) {
        resultInfoRepository.update(resultInfo);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        resultInfoRepository.delete(id);
    }

    @Override
    public Optional<ResultInfo> findById(Long id) {
        return resultInfoRepository.findById(id);
    }
}
