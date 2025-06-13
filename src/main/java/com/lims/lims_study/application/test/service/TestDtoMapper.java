package com.lims.lims_study.application.test.service;

import com.lims.lims_study.application.product.dto.ProductResponseDto;
import com.lims.lims_study.application.test.dto.TestResponseDto;
import com.lims.lims_study.application.user.dto.UserResponseDto;
import com.lims.lims_study.domain.product.model.Product;
import com.lims.lims_study.domain.test.model.*;
import com.lims.lims_study.domain.user.model.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TestDtoMapper {
    public TestResponseDto toResponseDto(Test test, User user, Product product,
                                         RequestInfo requestInfo, ReceiptInfo receiptInfo,
                                         ResultInfo resultInfo, CompletionInfo completionInfo) {
        return new TestResponseDto(
                test.getId(),
                mapRequestInfo(requestInfo),
                mapReceiptInfo(receiptInfo),
                mapResultInfo(resultInfo),
                mapCompletionInfo(completionInfo),
                mapUser(user),
                mapProduct(product),
                test.getStage(),
                test.getCreatedAt().toString(),
                mapUpdatedAt(test.getUpdatedAt())
        );
    }

    private TestResponseDto.RequestInfoDto mapRequestInfo(RequestInfo requestInfo) {
        if (requestInfo == null) {
            return null;
        }
        return new TestResponseDto.RequestInfoDto(
                requestInfo.getTitle(),
                requestInfo.getDescription(),
                requestInfo.isRequiresApproval()
        );
    }

    private TestResponseDto.ReceiptInfoDto mapReceiptInfo(ReceiptInfo receiptInfo) {
        if (receiptInfo == null) {
            return null;
        }
        return new TestResponseDto.ReceiptInfoDto(
                receiptInfo.getReceiptDetails(),
                receiptInfo.isRequiresApproval()
        );
    }

    private TestResponseDto.ResultInfoDto mapResultInfo(ResultInfo resultInfo) {
        if (resultInfo == null) {
            return null;
        }
        return new TestResponseDto.ResultInfoDto(
                resultInfo.getResultData(),
                resultInfo.isRequiresApproval()
        );
    }

    private TestResponseDto.CompletionInfoDto mapCompletionInfo(CompletionInfo completionInfo) {
        if (completionInfo == null) {
            return null;
        }
        return new TestResponseDto.CompletionInfoDto(
                completionInfo.getCompletionNotes(),
                completionInfo.isRequiresApproval()
        );
    }

    private UserResponseDto mapUser(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getAuthorities()
        );
    }

    private ProductResponseDto mapProduct(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCreatedAt().toString(),
                product.getUpdatedAt().toString()
        );
    }

    private String mapUpdatedAt(Object updatedAt) {
        return Optional.ofNullable(updatedAt)
                .map(Object::toString)
                .orElse(null);
    }
}