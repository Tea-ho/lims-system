package com.lims.lims_study.application.test.service;

import com.lims.lims_study.application.approval.dto.ApprovalCreateDto;
import com.lims.lims_study.application.approval.dto.ApprovalSignUpdateDto;
import com.lims.lims_study.application.test.dto.*;
import com.lims.lims_study.domain.product.model.Product;
import com.lims.lims_study.domain.product.service.IProductService;
import com.lims.lims_study.domain.test.model.ReceiptInfo;
import com.lims.lims_study.domain.test.model.RequestInfo;
import com.lims.lims_study.domain.test.model.Test;
import com.lims.lims_study.domain.test.service.ITestCrudService;
import com.lims.lims_study.domain.test.service.ITestReceiptCrudService;
import com.lims.lims_study.domain.test.service.ITestRequestCrudService;
import com.lims.lims_study.domain.test.service.ITestStateService;
import com.lims.lims_study.domain.user.model.User;
import com.lims.lims_study.domain.user.service.IUserService;
import com.lims.lims_study.global.util.AuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestApplicationService implements ITestApplicationService {
    private final ITestCrudService testCrudService;
    private final ITestStateService testStateService;
    private final ITestRequestCrudService testRequestCrudService;
    private final ITestReceiptCrudService testReceiptCrudService;
    private final IUserService userService;
    private final IProductService productService;
    
    private final AuthenticationProvider authProvider;
    private final TestDtoMapper dtoMapper;

    @Override
    public TestResponseDto createTest(TestCreateDto dto) {
        String username = authProvider.getCurrentUsername();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("UserRepository not found: " + username));
        Product product = productService.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("ProductRepository not found: " + dto.getProductId()));

        RequestInfo requestInfo = new RequestInfo(dto.getTitle(), dto.getDescription());
        Test test = new Test(user.getId(), product.getId(), requestInfo);

        testCrudService.createTest(test, requestInfo);
        return dtoMapper.toResponseDto(test, user, product, requestInfo, null, null, null);
    }

    @Override
    public TestResponseDto updateTest(Long testId, TestUpdateDto dto) {
        testCrudService.updateRequestInfo(testId, dto);
        Test test = testCrudService.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("TestRepository not found: " + testId));
        User user = userService.findById(test.getUserId())
                .orElse(createDefaultUser(test.getUserId()));
        Product product = productService.findById(test.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("ProductRepository not found: " + test.getProductId()));
        RequestInfo requestInfo = testRequestCrudService.findById(test.getRequestInfoId())
                .orElseThrow(() -> new IllegalArgumentException("RequestInfo not found"));
        return dtoMapper.toResponseDto(test, user, product, requestInfo, null, null, null);
    }

    @Override
    public void deleteTest(Long testId) {
        testCrudService.deleteTest(testId);
    }

    @Override
    public TestResponseDto getTest(Long testId) {
        Test test = testCrudService.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("TestRepository not found: " + testId));
        User user = userService.findById(test.getUserId())
                .orElse(createDefaultUser(test.getUserId()));
        Product product = productService.findById(test.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("ProductRepository not found: " + test.getProductId()));
        RequestInfo requestInfo = test.getRequestInfoId() != null ? testRequestCrudService.findById(test.getRequestInfoId()).orElse(null) : null;
        ReceiptInfo receiptInfo = test.getReceiptInfoId() != null ? testReceiptCrudService.findById(test.getReceiptInfoId()).orElse(null) : null;
        return dtoMapper.toResponseDto(test, user, product, requestInfo, receiptInfo, null, null);
    }

    @Override
    public List<TestResponseDto> searchTests(TestSearchDto dto) {
        List<Test> tests = testCrudService.findBySearchConditions(dto);
        return tests.stream().map(test -> {
            User user = userService.findById(test.getUserId())
                    .orElse(createDefaultUser(test.getUserId()));
            Product product = productService.findById(test.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("ProductRepository not found: " + test.getProductId()));
            RequestInfo requestInfo = test.getRequestInfoId() != null ? testRequestCrudService.findById(test.getRequestInfoId()).orElse(null) : null;
            ReceiptInfo receiptInfo = test.getReceiptInfoId() != null ? testReceiptCrudService.findById(test.getReceiptInfoId()).orElse(null) : null;
            return dtoMapper.toResponseDto(test, user, product, requestInfo, receiptInfo, null, null);
        }).collect(Collectors.toList());
    }

    private User createDefaultUser(Long userId) {
        return new User(userId, "알 수 없는 사용자", "defaultPassword", "ROLE_USER");
    }

    @Override
    public TestResponseDto moveToReceipt(Long testId, ReceiptCreateDto dto) {
        ReceiptInfo receiptInfo = new ReceiptInfo(dto.getReceiptDetails(), dto.isRequiresApproval());
        testStateService.moveToReceipt(testId, receiptInfo);
        Test test = testCrudService.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("TestRepository not found: " + testId));
        User user = userService.findById(test.getUserId())
                .orElse(createDefaultUser(test.getUserId()));
        Product product = productService.findById(test.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("ProductRepository not found: " + test.getProductId()));
        RequestInfo requestInfo = testRequestCrudService.findById(test.getRequestInfoId())
                .orElseThrow(() -> new IllegalStateException("Request info not found"));
        return dtoMapper.toResponseDto(test, user, product, requestInfo, receiptInfo, null, null);
    }

    @Override
    public TestResponseDto moveBackToRequest(Long testId) {
        testStateService.moveBackToRequest(testId);
        Test test = testCrudService.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("TestRepository not found: " + testId));
        User user = userService.findById(test.getUserId())
                .orElse(createDefaultUser(test.getUserId()));
        Product product = productService.findById(test.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("ProductRepository not found: " + test.getProductId()));
        RequestInfo requestInfo = testRequestCrudService.findById(test.getRequestInfoId())
                .orElseThrow(() -> new IllegalStateException("Request info not found"));
        return dtoMapper.toResponseDto(test, user, product, requestInfo, null, null, null);
    }

    @Override
    public TestResponseDto moveToReceiptApproval(Long testId, ApprovalCreateDto approvalDto) {
        testStateService.moveToReceiptApproval(testId, approvalDto);
        Test test = testCrudService.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Test not found: " + testId));
        User user = userService.findById(test.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + test.getUserId()));
        Product product = productService.findById(test.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + test.getProductId()));
        RequestInfo requestInfo = testRequestCrudService.findById(test.getRequestInfoId())
                .orElseThrow(() -> new IllegalStateException("Request info not found"));
        ReceiptInfo receiptInfo = testReceiptCrudService.findById(test.getReceiptInfoId())
                .orElseThrow(() -> new IllegalStateException("Receipt info not found"));
        return dtoMapper.toResponseDto(test, user, product, requestInfo, receiptInfo, null, null);
    }

    @Override
    public TestResponseDto moveBackToReceipt(Long testId, Long approvalId, ApprovalSignUpdateDto updateDto) {
        testStateService.moveBackToReceipt(testId, approvalId, updateDto);
        Test test = testCrudService.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Test not found: " + testId));
        User user = userService.findById(test.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + test.getUserId()));
        Product product = productService.findById(test.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + test.getProductId()));
        RequestInfo requestInfo = testRequestCrudService.findById(test.getRequestInfoId())
                .orElseThrow(() -> new IllegalStateException("Request info not found"));
        ReceiptInfo receiptInfo = testReceiptCrudService.findById(test.getReceiptInfoId())
                .orElseThrow(() -> new IllegalStateException("Receipt info not found"));
        return dtoMapper.toResponseDto(test, user, product, requestInfo, receiptInfo, null, null);
    }

    @Override
    public TestResponseDto moveToInputResult(Long testId, Long approvalId, ApprovalSignUpdateDto updateDto) {
        testStateService.moveToInputResult(testId, approvalId, updateDto);
        Test test = testCrudService.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Test not found: " + testId));
        User user = userService.findById(test.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + test.getUserId()));
        Product product = productService.findById(test.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + test.getProductId()));
        RequestInfo requestInfo = testRequestCrudService.findById(test.getRequestInfoId())
                .orElseThrow(() -> new IllegalStateException("Request info not found"));
        ReceiptInfo receiptInfo = testReceiptCrudService.findById(test.getReceiptInfoId())
                .orElseThrow(() -> new IllegalStateException("Receipt info not found"));
        return dtoMapper.toResponseDto(test, user, product, requestInfo, receiptInfo, null, null);
    }
}