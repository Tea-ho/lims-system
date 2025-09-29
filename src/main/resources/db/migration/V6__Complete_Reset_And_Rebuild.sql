-- 데이터베이스 완전 재설정 및 재구축
-- Version: 6.0

-- 외래키 제약 조건 비활성화
SET FOREIGN_KEY_CHECKS = 0;

-- 기존 모든 테이블 삭제
DROP TABLE IF EXISTS approval_signs;
DROP TABLE IF EXISTS approval_requests;
DROP TABLE IF EXISTS approvals;
DROP TABLE IF EXISTS completion_info;
DROP TABLE IF EXISTS result_info;
DROP TABLE IF EXISTS receipt_info;
DROP TABLE IF EXISTS request_info;
DROP TABLE IF EXISTS tests;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS audit_logs;

SET FOREIGN_KEY_CHECKS = 1;

-- 사용자 테이블
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role ENUM('USER', 'ADMIN', 'APPROVER') NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_role (role)
);

-- 제품 테이블
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_name (name),
    INDEX idx_created_at (created_at)
);

-- 시험 테이블
CREATE TABLE tests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    stage ENUM('REQUEST', 'RECEIPT', 'RECEIPT_APPROVAL', 'RESULT_INPUT', 'COMPLETED') NOT NULL DEFAULT 'REQUEST',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_product_id (product_id),
    INDEX idx_requester_id (requester_id),
    INDEX idx_stage (stage),
    INDEX idx_created_at (created_at),

    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (requester_id) REFERENCES users(id)
);

-- 시험 요청 정보 테이블
CREATE TABLE request_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    test_id BIGINT NOT NULL UNIQUE,
    test_items TEXT NOT NULL,
    priority ENUM('LOW', 'MEDIUM', 'HIGH', 'URGENT') NOT NULL DEFAULT 'MEDIUM',
    requested_date DATE NOT NULL,
    due_date DATE NOT NULL,
    special_instructions TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_test_id (test_id),
    INDEX idx_priority (priority),
    INDEX idx_requested_date (requested_date),
    INDEX idx_due_date (due_date),

    FOREIGN KEY (test_id) REFERENCES tests(id) ON DELETE CASCADE
);

-- 시험 접수 정보 테이블
CREATE TABLE receipt_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    test_id BIGINT NOT NULL UNIQUE,
    received_by BIGINT NOT NULL,
    received_date TIMESTAMP NOT NULL,
    sample_condition ENUM('GOOD', 'DAMAGED', 'INSUFFICIENT') NOT NULL DEFAULT 'GOOD',
    storage_location VARCHAR(255),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_test_id (test_id),
    INDEX idx_received_by (received_by),
    INDEX idx_received_date (received_date),
    INDEX idx_sample_condition (sample_condition),

    FOREIGN KEY (test_id) REFERENCES tests(id) ON DELETE CASCADE,
    FOREIGN KEY (received_by) REFERENCES users(id)
);

-- 시험 결과 정보 테이블
CREATE TABLE result_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    test_id BIGINT NOT NULL UNIQUE,
    tested_by BIGINT NOT NULL,
    test_started_date TIMESTAMP NOT NULL,
    test_completed_date TIMESTAMP,
    results JSON,
    pass_fail ENUM('PASS', 'FAIL', 'PENDING') NOT NULL DEFAULT 'PENDING',
    comments TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_test_id (test_id),
    INDEX idx_tested_by (tested_by),
    INDEX idx_test_started_date (test_started_date),
    INDEX idx_test_completed_date (test_completed_date),
    INDEX idx_pass_fail (pass_fail),

    FOREIGN KEY (test_id) REFERENCES tests(id) ON DELETE CASCADE,
    FOREIGN KEY (tested_by) REFERENCES users(id)
);

-- 시험 완료 정보 테이블
CREATE TABLE completion_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    test_id BIGINT NOT NULL UNIQUE,
    completed_by BIGINT NOT NULL,
    completion_date TIMESTAMP NOT NULL,
    report_path VARCHAR(500),
    final_approval_status ENUM('APPROVED', 'REJECTED', 'PENDING') NOT NULL DEFAULT 'PENDING',
    final_comments TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_test_id (test_id),
    INDEX idx_completed_by (completed_by),
    INDEX idx_completion_date (completion_date),
    INDEX idx_final_approval_status (final_approval_status),

    FOREIGN KEY (test_id) REFERENCES tests(id) ON DELETE CASCADE,
    FOREIGN KEY (completed_by) REFERENCES users(id)
);

-- 승인 마스터 테이블 (새로운 구조)
CREATE TABLE approvals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    status ENUM('PENDING', 'PARTIAL_APPROVED', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_status (status),
    INDEX idx_version (version),
    INDEX idx_created_at (created_at),
    INDEX idx_status_created (status, created_at)
);

-- 승인 요청 테이블
CREATE TABLE approval_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    approval_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_approval_id (approval_id),
    INDEX idx_requester_id (requester_id),
    INDEX idx_created_at (created_at),

    FOREIGN KEY (approval_id) REFERENCES approvals(id) ON DELETE CASCADE,
    FOREIGN KEY (requester_id) REFERENCES users(id),

    UNIQUE KEY uk_approval_request (approval_id)
);

-- 승인 서명 테이블
CREATE TABLE approval_signs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    approval_id BIGINT NOT NULL,
    approver_id BIGINT NOT NULL,
    target_id BIGINT NOT NULL,
    stage ENUM('REQUEST', 'RECEIPT', 'RECEIPT_APPROVAL', 'RESULT_INPUT', 'COMPLETED') NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_approval_id (approval_id),
    INDEX idx_approver_id (approver_id),
    INDEX idx_target_id (target_id),
    INDEX idx_stage (stage),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    INDEX idx_approval_status (approval_id, status),
    INDEX idx_target_stage (target_id, stage),

    FOREIGN KEY (approval_id) REFERENCES approvals(id) ON DELETE CASCADE,
    FOREIGN KEY (approver_id) REFERENCES users(id),

    UNIQUE KEY uk_approval_target_stage (approval_id, target_id, stage)
);

-- 감사 로그 테이블
CREATE TABLE audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100) NOT NULL,
    entity_id BIGINT,
    old_values JSON,
    new_values JSON,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_user_id (user_id),
    INDEX idx_action (action),
    INDEX idx_entity_type (entity_type),
    INDEX idx_entity_id (entity_id),
    INDEX idx_created_at (created_at),

    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 동시성 제어를 위한 트리거 (버전 자동 증가)
DELIMITER $$
CREATE TRIGGER approval_version_trigger
    BEFORE UPDATE ON approvals
    FOR EACH ROW
BEGIN
    SET NEW.version = OLD.version + 1;
END$$
DELIMITER ;

-- 승인 통계 뷰
CREATE OR REPLACE VIEW approval_statistics AS
SELECT
    a.status,
    COUNT(*) as approval_count,
    AVG(TIMESTAMPDIFF(HOUR, a.created_at, a.updated_at)) as avg_processing_hours,
    COUNT(CASE WHEN a.created_at >= DATE_SUB(NOW(), INTERVAL 24 HOUR) THEN 1 END) as daily_count,
    COUNT(CASE WHEN a.created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY) THEN 1 END) as weekly_count
FROM approvals a
GROUP BY a.status;

-- 승인자 워크로드 뷰
CREATE OR REPLACE VIEW approver_workload AS
SELECT
    u.id,
    u.username,
    COUNT(s.id) as total_signs,
    COUNT(CASE WHEN s.status = 'PENDING' THEN 1 END) as pending_signs,
    COUNT(CASE WHEN s.status = 'APPROVED' THEN 1 END) as approved_signs,
    COUNT(CASE WHEN s.status = 'REJECTED' THEN 1 END) as rejected_signs,
    AVG(CASE WHEN s.status != 'PENDING'
        THEN TIMESTAMPDIFF(HOUR, s.created_at, s.updated_at) END) as avg_response_hours
FROM users u
LEFT JOIN approval_signs s ON u.id = s.approver_id
GROUP BY u.id, u.username;