-- 승인 시스템 테이블 생성
USE lims_study;

-- 기존 테이블 확인 후 생성
SET FOREIGN_KEY_CHECKS = 0;

-- 승인 마스터 테이블
CREATE TABLE IF NOT EXISTS approvals (
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
CREATE TABLE IF NOT EXISTS approval_requests (
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
CREATE TABLE IF NOT EXISTS approval_signs (
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

SET FOREIGN_KEY_CHECKS = 1;

-- 테이블 생성 확인
SELECT 'Approval tables created successfully!' AS message;
SHOW TABLES LIKE '%approval%';