package com.lims.lims_study.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "C002", "엔티티를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", "서버 오류가 발생했습니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C004", "잘못된 타입 값입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "C005", "접근이 거부되었습니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다."),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "U002", "이미 존재하는 사용자명입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "U003", "잘못된 비밀번호입니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "U004", "인증되지 않은 사용자입니다."),

    // Product
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "제품을 찾을 수 없습니다."),
    DUPLICATE_PRODUCT_NAME(HttpStatus.BAD_REQUEST, "P002", "이미 존재하는 제품명입니다."),

    // Test
    TEST_NOT_FOUND(HttpStatus.NOT_FOUND, "T001", "시험을 찾을 수 없습니다."),
    INVALID_TEST_STAGE(HttpStatus.BAD_REQUEST, "T002", "유효하지 않은 시험 단계입니다."),
    CANNOT_MOVE_TO_NEXT_STAGE(HttpStatus.BAD_REQUEST, "T003", "다음 단계로 이동할 수 없습니다."),
    CANNOT_MOVE_TO_PREVIOUS_STAGE(HttpStatus.BAD_REQUEST, "T004", "이전 단계로 이동할 수 없습니다."),
    TEST_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "T005", "이미 완료된 시험입니다."),

    // Approval
    APPROVAL_NOT_FOUND(HttpStatus.NOT_FOUND, "A001", "결재를 찾을 수 없습니다."),
    APPROVAL_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "A002", "이미 처리된 결재입니다."),
    INVALID_APPROVER(HttpStatus.BAD_REQUEST, "A003", "유효하지 않은 결재자입니다."),

    // Auth
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH002", "만료된 토큰입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH003", "잘못된 인증 정보입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
