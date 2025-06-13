package com.lims.lims_study.domain.test.service;

import com.lims.lims_study.domain.product.repository.ProductRepository;
import com.lims.lims_study.domain.test.model.ReceiptInfo;
import com.lims.lims_study.domain.test.model.Test;
import com.lims.lims_study.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestValidator {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    public void validateUserAndProductExists(Test test) {
        userRepository.findById(test.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + test.getUserId()));
        productRepository.findById(test.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + test.getProductId()));
    }

    public Test validateTestExists(Long testId, ITestCrudService testCrudService) {
        return testCrudService.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Test not found: " + testId));
    }

    public ReceiptInfo validateReciptInfoExists(Long reciptInfoId, ITestReceiptCrudService testReceiptCrudService){
        return testReceiptCrudService.findById(reciptInfoId)
                .orElseThrow(() -> new IllegalStateException("Receipt info not found"));
    }

}