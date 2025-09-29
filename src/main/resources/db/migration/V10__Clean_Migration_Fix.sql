-- Complete database reset and rebuild with test data
-- Version: 10.0
-- 대시보드 데이터 및 시험 단계별 데이터 포함 전체 스키마 재구성

SET FOREIGN_KEY_CHECKS = 0;

-- 기존 테이블들 삭제 (순서 고려)
DROP TABLE IF EXISTS audit_logs;
DROP TABLE IF EXISTS tests;
DROP TABLE IF EXISTS approval_signs;
DROP TABLE IF EXISTS approval_requests;
DROP TABLE IF EXISTS approvals;
DROP TABLE IF EXISTS completion_infos;
DROP TABLE IF EXISTS result_infos;
DROP TABLE IF EXISTS receipt_infos;
DROP TABLE IF EXISTS request_infos;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS flyway_schema_history;

SET FOREIGN_KEY_CHECKS = 1;

-- 사용자 테이블
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_username (username),
    INDEX idx_role (role)
);

-- 제품 테이블
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_name (name)
);

-- 시험 요청 정보 테이블
CREATE TABLE request_infos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    is_urgent BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_title (title),
    INDEX idx_urgent (is_urgent),
    INDEX idx_created_at (created_at)
);

-- 시험 접수 정보 테이블
CREATE TABLE receipt_infos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receipt_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    responsible_person VARCHAR(100),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 시험 결과 정보 테이블
CREATE TABLE result_infos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    test_result TEXT,
    conclusion VARCHAR(500),
    test_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 시험 완료 정보 테이블
CREATE TABLE completion_infos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    completion_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    final_report TEXT,
    approved_by VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 승인 테이블
CREATE TABLE approvals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_status (status)
);

-- 승인 요청 테이블
CREATE TABLE approval_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    approval_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (approval_id) REFERENCES approvals(id) ON DELETE CASCADE,
    FOREIGN KEY (requester_id) REFERENCES users(id),
    UNIQUE KEY uk_approval_request (approval_id)
);

-- 승인 서명 테이블
CREATE TABLE approval_signs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    approval_id BIGINT NOT NULL,
    signer_id BIGINT NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    comment TEXT,
    signed_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (approval_id) REFERENCES approvals(id) ON DELETE CASCADE,
    FOREIGN KEY (signer_id) REFERENCES users(id),
    INDEX idx_approval_id (approval_id),
    INDEX idx_signer_id (signer_id),
    INDEX idx_status (status)
);

-- 시험 메인 테이블
CREATE TABLE tests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_info_id BIGINT,
    receipt_info_id BIGINT,
    result_info_id BIGINT,
    completion_info_id BIGINT,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    stage ENUM('REQUEST', 'RECEIPT', 'RECEIPT_APPROVAL', 'RESULT_INPUT', 'COMPLETED') DEFAULT 'REQUEST',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (request_info_id) REFERENCES request_infos(id),
    FOREIGN KEY (receipt_info_id) REFERENCES receipt_infos(id),
    FOREIGN KEY (result_info_id) REFERENCES result_infos(id),
    FOREIGN KEY (completion_info_id) REFERENCES completion_infos(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id),

    INDEX idx_stage (stage),
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id),
    INDEX idx_created_at (created_at)
);

-- 감사 로그 테이블
CREATE TABLE audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    action VARCHAR(50) NOT NULL,
    target_table VARCHAR(50),
    target_id BIGINT,
    details JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_action (action),
    INDEX idx_target (target_table, target_id),
    INDEX idx_created_at (created_at)
);

-- 기초 데이터 삽입
-- 사용자 데이터
INSERT INTO users (username, password, role) VALUES
('admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'ROLE_ADMIN'),
('user1', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'ROLE_USER'),
('user2', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'ROLE_USER'),
('user3', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'ROLE_USER');

-- 제품 데이터
INSERT INTO products (name, description) VALUES
('제품 A', '첫 번째 테스트 제품입니다.'),
('제품 B', '두 번째 테스트 제품입니다.'),
('제품 C', '세 번째 테스트 제품입니다.'),
('제품 D', '네 번째 테스트 제품입니다.'),
('제품 E', '다섯 번째 테스트 제품입니다.');

-- 승인 데이터 (대시보드용)
INSERT INTO approvals (status) VALUES
('PENDING'),
('PENDING'),
('PENDING'),
('APPROVED'),
('APPROVED'),
('REJECTED');

-- RequestInfo 샘플 데이터
INSERT INTO request_infos (title, description, is_urgent, created_at, updated_at) VALUES
('제품 A 품질 검사', '제품 A에 대한 정기 품질 검사입니다.', false, NOW(), NOW()),
('제품 B 안전성 테스트', '제품 B의 안전성을 확인하기 위한 테스트입니다.', true, NOW(), NOW()),
('제품 C 성능 평가', '제품 C의 성능을 평가합니다.', false, NOW(), NOW()),
('제품 D 내구성 검사', '제품 D의 내구성을 검사합니다.', true, NOW(), NOW()),
('제품 E 호환성 테스트', '제품 E의 호환성을 테스트합니다.', false, NOW(), NOW()),
('제품 A 추가 검사', '제품 A에 대한 추가 검사입니다.', false, NOW(), NOW()),
('제품 B 재검사', '제품 B의 재검사입니다.', true, NOW(), NOW()),
('제품 C 최종 점검', '제품 C의 최종 점검입니다.', false, NOW(), NOW());

-- 각 단계별로 시험 데이터 생성
-- REQUEST 단계 (2개)
INSERT INTO tests (request_info_id, receipt_info_id, result_info_id, completion_info_id, user_id, product_id, stage, created_at, updated_at) VALUES
(1, NULL, NULL, NULL, 1, 1, 'REQUEST', NOW(), NOW()),
(2, NULL, NULL, NULL, 2, 2, 'REQUEST', NOW(), NOW());

-- RECEIPT 단계 (2개)
INSERT INTO tests (request_info_id, receipt_info_id, result_info_id, completion_info_id, user_id, product_id, stage, created_at, updated_at) VALUES
(3, NULL, NULL, NULL, 3, 3, 'RECEIPT', NOW(), NOW()),
(4, NULL, NULL, NULL, 1, 4, 'RECEIPT', NOW(), NOW());

-- RECEIPT_APPROVAL 단계 (1개)
INSERT INTO tests (request_info_id, receipt_info_id, result_info_id, completion_info_id, user_id, product_id, stage, created_at, updated_at) VALUES
(5, NULL, NULL, NULL, 2, 5, 'RECEIPT_APPROVAL', NOW(), NOW());

-- RESULT_INPUT 단계 (2개)
INSERT INTO tests (request_info_id, receipt_info_id, result_info_id, completion_info_id, user_id, product_id, stage, created_at, updated_at) VALUES
(6, NULL, NULL, NULL, 3, 1, 'RESULT_INPUT', NOW(), NOW()),
(7, NULL, NULL, NULL, 1, 2, 'RESULT_INPUT', NOW(), NOW());

-- COMPLETED 단계 (1개)
INSERT INTO tests (request_info_id, receipt_info_id, result_info_id, completion_info_id, user_id, product_id, stage, created_at, updated_at) VALUES
(8, NULL, NULL, NULL, 2, 3, 'COMPLETED', NOW(), NOW());