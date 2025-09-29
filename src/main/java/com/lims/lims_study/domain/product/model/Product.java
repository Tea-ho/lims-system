package com.lims.lims_study.domain.product.model;

import com.lims.lims_study.application.product.dto.ProductUpdateDto;
import com.lims.lims_study.global.exception.BusinessException;
import com.lims.lims_study.global.exception.ErrorCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Product {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product(Long id, String name, String description) {
        validateCreationInputs(name, description);
        
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateProductInfo(ProductUpdateDto dto) {
        validateUpdateInputs(dto);
        
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateProductInfo(String name, String description) {
        validateName(name);
        validateDescription(description);
        
        this.name = name;
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    // 비즈니스 로직
    public boolean isSameProduct(Long productId) {
        return Objects.equals(this.id, productId);
    }

    public boolean hasName(String name) {
        return Objects.equals(this.name, name);
    }

    // Setters (MyBatis용)
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // 검증 메서드
    private void validateCreationInputs(String name, String description) {
        validateName(name);
        validateDescription(description);
    }

    private void validateUpdateInputs(ProductUpdateDto dto) {
        if (dto == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "제품 수정 정보는 필수입니다.");
        }
        validateName(dto.getName());
        validateDescription(dto.getDescription());
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "제품명은 필수입니다.");
        }
        if (name.length() > 100) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "제품명은 100자 이하여야 합니다.");
        }
    }

    private void validateDescription(String description) {
        if (description != null && description.length() > 500) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "제품 설명은 500자 이하여야 합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
