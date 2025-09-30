-- 수동으로 실행할 데이터 정리 스크립트

SET FOREIGN_KEY_CHECKS = 0;

-- 1. receipt_infos 테이블 구조 확인
SHOW COLUMNS FROM receipt_infos;

-- 2. receipt_infos 테이블 데이터 확인
SELECT * FROM receipt_infos LIMIT 5;

-- 3. 기존 데이터 삭제
TRUNCATE TABLE receipt_infos;
TRUNCATE TABLE result_infos;
TRUNCATE TABLE completion_infos;

-- 4. tests 테이블에서 외래키 NULL 처리
UPDATE tests SET receipt_info_id = NULL;
UPDATE tests SET result_info_id = NULL;
UPDATE tests SET completion_info_id = NULL;

-- 5. receipt_infos 테이블 구조가 잘못되어 있다면 재생성
DROP TABLE IF EXISTS receipt_infos;

CREATE TABLE receipt_infos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receipt_details TEXT,
    requires_approval BOOLEAN DEFAULT FALSE,
    approval_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_approval_id (approval_id),
    INDEX idx_requires_approval (requires_approval)
);

-- 6. result_infos 테이블 재생성
DROP TABLE IF EXISTS result_infos;

CREATE TABLE result_infos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    result_data TEXT,
    requires_approval BOOLEAN DEFAULT FALSE,
    approval_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_approval_id (approval_id),
    INDEX idx_requires_approval (requires_approval)
);

-- 7. completion_infos 테이블 재생성
DROP TABLE IF EXISTS completion_infos;

CREATE TABLE completion_infos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    completion_notes TEXT,
    requires_approval BOOLEAN DEFAULT FALSE,
    approval_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_approval_id (approval_id),
    INDEX idx_requires_approval (requires_approval)
);

-- 8. request_infos 테이블 수정
ALTER TABLE request_infos
ADD COLUMN IF NOT EXISTS requires_approval BOOLEAN DEFAULT FALSE;

SET FOREIGN_KEY_CHECKS = 1;

-- 확인
SELECT 'receipt_infos' as table_name, COUNT(*) as count FROM receipt_infos
UNION ALL
SELECT 'result_infos', COUNT(*) FROM result_infos
UNION ALL
SELECT 'completion_infos', COUNT(*) FROM completion_infos;