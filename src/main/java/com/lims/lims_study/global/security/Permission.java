package com.lims.lims_study.global.security;

public enum Permission {
    // User permissions
    USER_READ("user:read", "사용자 조회"),
    USER_WRITE("user:write", "사용자 생성/수정"),
    USER_DELETE("user:delete", "사용자 삭제"),
    
    // Product permissions
    PRODUCT_READ("product:read", "제품 조회"),
    PRODUCT_WRITE("product:write", "제품 생성/수정"),
    PRODUCT_DELETE("product:delete", "제품 삭제"),
    
    // Test permissions
    TEST_READ("test:read", "시험 조회"),
    TEST_WRITE("test:write", "시험 생성/수정"),
    TEST_DELETE("test:delete", "시험 삭제"),
    TEST_EXECUTE("test:execute", "시험 실행"),
    TEST_APPROVE("test:approve", "시험 승인"),
    
    // Approval permissions
    APPROVAL_READ("approval:read", "결재 조회"),
    APPROVAL_WRITE("approval:write", "결재 생성/수정"),
    APPROVAL_PROCESS("approval:process", "결재 처리"),
    
    // Admin permissions
    ADMIN_READ("admin:read", "관리자 조회"),
    ADMIN_WRITE("admin:write", "관리자 설정"),
    SYSTEM_CONFIG("system:config", "시스템 설정");

    private final String permission;
    private final String description;

    Permission(String permission, String description) {
        this.permission = permission;
        this.description = description;
    }

    public String getPermission() {
        return permission;
    }

    public String getDescription() {
        return description;
    }
}
