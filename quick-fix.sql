-- 빠른 수정 스크립트
-- MySQL 클라이언트에서 직접 실행하세요
-- mysql -u lims_study -p lims_study < quick-fix.sql

USE lims_study;

SET FOREIGN_KEY_CHECKS = 0;

-- 1. 테이블 재생성
DROP TABLE IF EXISTS receipt_infos;
DROP TABLE IF EXISTS result_infos;
DROP TABLE IF EXISTS completion_infos;

CREATE TABLE receipt_infos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receipt_details TEXT,
    requires_approval BOOLEAN DEFAULT FALSE,
    approval_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_approval_id (approval_id)
);

CREATE TABLE result_infos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    result_data TEXT,
    requires_approval BOOLEAN DEFAULT FALSE,
    approval_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_approval_id (approval_id)
);

CREATE TABLE completion_infos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    completion_notes TEXT,
    requires_approval BOOLEAN DEFAULT FALSE,
    approval_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_approval_id (approval_id)
);

-- 2. request_infos 테이블에 컬럼 추가
ALTER TABLE request_infos ADD COLUMN IF NOT EXISTS requires_approval BOOLEAN DEFAULT FALSE;

-- 3. tests 테이블 외래키 NULL 처리
UPDATE tests SET receipt_info_id = NULL;
UPDATE tests SET result_info_id = NULL;
UPDATE tests SET completion_info_id = NULL;

SET FOREIGN_KEY_CHECKS = 1;

SELECT 'Fix completed!' as status;