package com.lims.lims_study.domain.test.model;

import com.lims.lims_study.global.exception.BusinessException;
import com.lims.lims_study.global.exception.ErrorCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Test {
    private Long id;
    private Long requestInfoId;
    private Long receiptInfoId;
    private Long resultInfoId;
    private Long completionInfoId;
    private Long userId;
    private Long productId;
    private String stage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // MyBatis용 기본 생성자
    public Test() {
    }

    // 비즈니스 로직용 생성자
    public Test(Long userId, Long productId, RequestInfo requestInfo) {
        validateCreationInputs(userId, productId, requestInfo);

        this.userId = userId;
        this.productId = productId;
        // requestInfoId는 나중에 TestCrudService에서 설정됨 (requestInfo 저장 후)
        this.requestInfoId = null;
        this.stage = TestStage.REQUEST.name();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 상태 전이 메서드
    public void moveToNextStage() {
        TestStage currentStage = getCurrentTestStage();
        TestStage nextStage = currentStage.next();
        
        if (nextStage == null) {
            throw new BusinessException(ErrorCode.CANNOT_MOVE_TO_NEXT_STAGE, 
                    "현재 단계(" + currentStage + ")에서 다음 단계로 이동할 수 없습니다.");
        }
        
        this.stage = nextStage.name();
        this.updatedAt = LocalDateTime.now();
    }

    public void moveToPreviousStage() {
        TestStage currentStage = getCurrentTestStage();
        TestStage previousStage = currentStage.previous();
        
        if (previousStage == null) {
            throw new BusinessException(ErrorCode.CANNOT_MOVE_TO_PREVIOUS_STAGE, 
                    "현재 단계(" + currentStage + ")에서 이전 단계로 이동할 수 없습니다.");
        }
        
        this.stage = previousStage.name();
        this.updatedAt = LocalDateTime.now();
    }

    public void completeTest() {
        if (!isCompleted()) {
            throw new BusinessException(ErrorCode.INVALID_TEST_STAGE, 
                    "완료된 단계에서만 시험을 완료할 수 있습니다.");
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void assignReceiptInfo(Long receiptInfoId) {
        validateNotNull(receiptInfoId, "접수 정보 ID는 필수입니다.");
        this.receiptInfoId = receiptInfoId;
        this.updatedAt = LocalDateTime.now();
    }

    public void assignResultInfo(Long resultInfoId) {
        validateNotNull(resultInfoId, "결과 정보 ID는 필수입니다.");
        this.resultInfoId = resultInfoId;
        this.updatedAt = LocalDateTime.now();
    }

    public void assignCompletionInfo(Long completionInfoId) {
        validateNotNull(completionInfoId, "완료 정보 ID는 필수입니다.");
        this.completionInfoId = completionInfoId;
        this.updatedAt = LocalDateTime.now();
    }

    // 상태 확인 메서드
    public boolean isInStage(TestStage testStage) {
        return testStage.name().equals(this.stage);
    }

    public boolean isCompleted() {
        return TestStage.COMPLETED.name().equals(this.stage);
    }

    public boolean canMoveToNext() {
        TestStage currentStage = getCurrentTestStage();
        return currentStage.next() != null;
    }

    public boolean canMoveToPrevious() {
        TestStage currentStage = getCurrentTestStage();
        return currentStage.previous() != null;
    }

    public TestStage getCurrentTestStage() {
        try {
            return TestStage.valueOf(this.stage);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_TEST_STAGE, 
                    "유효하지 않은 시험 단계입니다: " + this.stage);
        }
    }

    // 비즈니스 규칙 검증
    public boolean belongsToUser(Long userId) {
        return Objects.equals(this.userId, userId);
    }

    public boolean isForProduct(Long productId) {
        return Objects.equals(this.productId, productId);
    }

    // Setters (MyBatis용)
    public void setId(Long id) { this.id = id; }
    public void setRequestInfoId(Long requestInfoId) { this.requestInfoId = requestInfoId; }
    public void setReceiptInfoId(Long receiptInfoId) { this.receiptInfoId = receiptInfoId; }
    public void setResultInfoId(Long resultInfoId) { this.resultInfoId = resultInfoId; }
    public void setCompletionInfoId(Long completionInfoId) { this.completionInfoId = completionInfoId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public void setStage(String stage) { this.stage = stage; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // 검증 메서드
    private void validateCreationInputs(Long userId, Long productId, RequestInfo requestInfo) {
        validateNotNull(userId, "사용자 ID는 필수입니다.");
        validateNotNull(productId, "제품 ID는 필수입니다.");
        validateNotNull(requestInfo, "의뢰 정보는 필수입니다.");
        // RequestInfo ID는 생성 시점에는 null일 수 있으므로 validation 제거
    }

    private void validateNotNull(Object value, String message) {
        if (value == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, message);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Test test = (Test) o;
        return Objects.equals(id, test.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", stage='" + stage + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
