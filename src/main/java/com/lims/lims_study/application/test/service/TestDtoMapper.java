package com.lims.lims_study.application.test.service;

import com.lims.lims_study.application.product.dto.ProductResponseDto;
import com.lims.lims_study.application.test.dto.TestResponseDto;
import com.lims.lims_study.application.user.dto.UserResponseDto;
import com.lims.lims_study.domain.product.model.Product;
import com.lims.lims_study.domain.test.model.*;
import com.lims.lims_study.domain.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class TestDtoMapper {
    public TestResponseDto toResponseDto(Test test, User user, Product product,
                                         RequestInfo requestInfo, ReceiptInfo receiptInfo,
                                         ResultInfo resultInfo, CompletionInfo completionInfo) {
        return new TestResponseDto(
                test.getId(),
                requestInfo != null ? new TestResponseDto.RequestInfoDto(
                        requestInfo.getTitle(), requestInfo.getDescription(), requestInfo.isRequiresApproval()) : null,
                receiptInfo != null ? new TestResponseDto.ReceiptInfoDto(
                        receiptInfo.getReceiptDetails(), receiptInfo.isRequiresApproval()) : null,
                resultInfo != null ? new TestResponseDto.ResultInfoDto(
                        resultInfo.getResultData(), resultInfo.isRequiresApproval()) : null,
                completionInfo != null ? new TestResponseDto.CompletionInfoDto(
                        completionInfo.getCompletionNotes(), completionInfo.isRequiresApproval()) : null,
                new UserResponseDto(user.getId(), user.getUsername(), user.getAuthorities()),
                new ProductResponseDto(product.getId(), product.getName(), product.getDescription(), product.getCreatedAt().toString(), product.getUpdatedAt().toString()),
                test.getStage(),
                test.getCreatedAt().toString(),
                test.getUpdatedAt() != null ? test.getUpdatedAt().toString() : null
        );
    }
}