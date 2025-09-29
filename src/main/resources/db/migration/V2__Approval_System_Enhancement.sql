-- 승인 시스템 전원 승인제 및 동시성 제어 개선
-- Version: 2.0

-- 기존 승인 관련 테이블 삭제 (새로운 구조로 재생성)
-- Foreign key 제약 조건을 고려하여 역순으로 삭제
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS approval_signs;
DROP TABLE IF EXISTS approval_requests;
DROP TABLE IF EXISTS approvals;
SET FOREIGN_KEY_CHECKS = 1;

-- 승인 마스터 테이블 (새로운 구조)
CREATE TABLE approvals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    status ENUM('PENDING', 'PARTIAL_APPROVED', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    version BIGINT NOT NULL DEFAULT 0,  -- 동시성 제어를 위한 버전 필드
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- 인덱스
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
    
    -- 인덱스
    INDEX idx_approval_id (approval_id),
    INDEX idx_requester_id (requester_id),
    INDEX idx_created_at (created_at),
    
    -- 외래키
    FOREIGN KEY (approval_id) REFERENCES approvals(id) ON DELETE CASCADE,
    FOREIGN KEY (requester_id) REFERENCES users(id),
    
    -- 승인별 요청 유일성 보장
    UNIQUE KEY uk_approval_request (approval_id)
);

-- 승인 서명 테이블
CREATE TABLE approval_signs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    approval_id BIGINT NOT NULL,
    approver_id BIGINT NOT NULL,
    target_id BIGINT NOT NULL,  -- 승인 대상 ID (Test, User, Product 등)
    stage ENUM('REQUEST', 'RECEIPT', 'RECEIPT_APPROVAL', 'RESULT_INPUT', 'COMPLETED') NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- 인덱스
    INDEX idx_approval_id (approval_id),
    INDEX idx_approver_id (approver_id),
    INDEX idx_target_id (target_id),
    INDEX idx_stage (stage),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    INDEX idx_approval_status (approval_id, status),
    INDEX idx_target_stage (target_id, stage),
    
    -- 외래키
    FOREIGN KEY (approval_id) REFERENCES approvals(id) ON DELETE CASCADE,
    FOREIGN KEY (approver_id) REFERENCES users(id),
    
    -- 중복 승인 방지 (같은 승인에서 같은 대상과 단계에 대해 한 번만 승인 가능)
    UNIQUE KEY uk_approval_target_stage (approval_id, target_id, stage)
);

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

-- 동시성 제어를 위한 트리거 (버전 자동 증가)
DELIMITER $$
CREATE TRIGGER approval_version_trigger 
    BEFORE UPDATE ON approvals
    FOR EACH ROW
BEGIN
    SET NEW.version = OLD.version + 1;
END$$
DELIMITER ;
