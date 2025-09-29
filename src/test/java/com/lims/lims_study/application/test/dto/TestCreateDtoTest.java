package com.lims.lims_study.application.test.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@DisplayName("TestCreateDto 단위 테스트")
class TestCreateDtoTest {

    private Validator validator;
    private TestCreateDto testCreateDto;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        testCreateDto = new TestCreateDto();
    }

    @Test
    @DisplayName("유효한 TestCreateDto 생성 테스트")
    void validTestCreateDto() {
        // Given
        testCreateDto.setTitle("테스트 제목");
        testCreateDto.setDescription("테스트 설명");
        testCreateDto.setProductId(1L);

        // When
        Set<ConstraintViolation<TestCreateDto>> violations = validator.validate(testCreateDto);

        // Then
        assertThat(violations).isEmpty();
        assertThat(testCreateDto.getTitle()).isEqualTo("테스트 제목");
        assertThat(testCreateDto.getDescription()).isEqualTo("테스트 설명");
        assertThat(testCreateDto.getProductId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("제목이 없는 경우 검증 실패")
    void titleIsNull() {
        // Given
        testCreateDto.setTitle(null);
        testCreateDto.setDescription("테스트 설명");
        testCreateDto.setProductId(1L);

        // When
        Set<ConstraintViolation<TestCreateDto>> violations = validator.validate(testCreateDto);

        // Then
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("제품 ID가 없는 경우 검증 실패")
    void productIdIsNull() {
        // Given
        testCreateDto.setTitle("테스트 제목");
        testCreateDto.setDescription("테스트 설명");
        testCreateDto.setProductId(null);

        // When
        Set<ConstraintViolation<TestCreateDto>> violations = validator.validate(testCreateDto);

        // Then
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("DTO 필드 getter/setter 동작 확인")
    void getterSetterTest() {
        // Given
        String title = "테스트 제목";
        String description = "테스트 설명";
        Long productId = 123L;

        // When
        testCreateDto.setTitle(title);
        testCreateDto.setDescription(description);
        testCreateDto.setProductId(productId);

        // Then
        assertThat(testCreateDto.getTitle()).isEqualTo(title);
        assertThat(testCreateDto.getDescription()).isEqualTo(description);
        assertThat(testCreateDto.getProductId()).isEqualTo(productId);
    }
}
