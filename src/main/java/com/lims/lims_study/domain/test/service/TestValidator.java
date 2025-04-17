package com.lims.lims_study.domain.test.service;

import com.lims.lims_study.domain.product.repository.ProductMapper;
import com.lims.lims_study.domain.test.model.Test;
import com.lims.lims_study.domain.user.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestValidator {
    private final UserMapper userMapper;
    private final ProductMapper productMapper;

    public void validateCreate(Test test) {
        userMapper.findById(test.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + test.getUserId()));
        productMapper.findById(test.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + test.getProductId()));
    }

    public Test validateTestExists(Long testId, ITestCrudService testCrudService) {
        return testCrudService.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Test not found: " + testId));
    }
}