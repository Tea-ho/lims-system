-- 완전 재설정 및 샘플 데이터 삽입
-- Version: 8.0

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

-- 승인 마스터 테이블
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

-- 샘플 데이터 삽입

-- 사용자 샘플 데이터
INSERT INTO users (username, password, email, role) VALUES
('admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'admin@lims.com', 'ADMIN'),
('김철수', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'kim.cheolsu@lims.com', 'APPROVER'),
('박영희', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'park.younghee@lims.com', 'APPROVER'),
('이민수', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'lee.minsu@lims.com', 'USER'),
('최지영', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'choi.jiyoung@lims.com', 'USER'),
('정우진', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'jung.woojin@lims.com', 'APPROVER'),
('한소희', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'han.sohee@lims.com', 'USER'),
('윤태호', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'yoon.taeho@lims.com', 'USER'),
('서지혜', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'seo.jihye@lims.com', 'APPROVER'),
('강동원', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'kang.dongwon@lims.com', 'USER');

-- 제품 샘플 데이터
INSERT INTO products (name, description) VALUES
('아스피린 정제', '해열, 진통, 소염 효과가 있는 의약품'),
('비타민 C 캡슐', '면역력 강화를 위한 비타민 C 보충제'),
('오메가-3 소프트겔', '심혈관 건강을 위한 오메가-3 지방산 보충제'),
('유산균 분말', '장내 미생물 균형을 위한 프로바이오틱스'),
('칼슘 마그네슘 정제', '뼈 건강을 위한 칼슘과 마그네슘 복합제'),
('종합비타민 정제', '일일 필수 비타민과 미네랄 복합체'),
('단백질 파우더', '근육 발달을 위한 고단백 보충제'),
('철분 캡슐', '철결핍성 빈혈 예방을 위한 철분 보충제'),
('콜라겐 분말', '피부와 관절 건강을 위한 콜라겐 보충제'),
('루테인 캡슐', '눈 건강을 위한 루테인 보충제'),
('글루코사민 정제', '관절 건강을 위한 글루코사민 보충제'),
('코엔자임 Q10 캡슐', '세포 에너지 생산을 위한 코엔자임 Q10'),
('마그네슘 분말', '근육과 신경 기능을 위한 마그네슘 보충제'),
('비타민 D3 캡슐', '뼈 건강과 면역력을 위한 비타민 D3'),
('셀레늄 정제', '항산화 효과를 위한 셀레늄 보충제');

-- 시험 샘플 데이터
INSERT INTO tests (product_id, requester_id, stage) VALUES
(1, 4, 'COMPLETED'),
(2, 4, 'RESULT_INPUT'),
(3, 5, 'RECEIPT_APPROVAL'),
(4, 5, 'RECEIPT'),
(5, 7, 'REQUEST'),
(6, 7, 'COMPLETED'),
(7, 8, 'RESULT_INPUT'),
(8, 8, 'RECEIPT_APPROVAL'),
(9, 10, 'RECEIPT'),
(10, 10, 'REQUEST'),
(11, 4, 'COMPLETED'),
(12, 5, 'RESULT_INPUT'),
(13, 7, 'RECEIPT_APPROVAL'),
(14, 8, 'RECEIPT'),
(15, 10, 'REQUEST');

-- 시험 요청 정보 샘플 데이터
INSERT INTO request_info (test_id, test_items, priority, requested_date, due_date, special_instructions) VALUES
(1, '성분 함량, 순도 시험, 용출 시험', 'HIGH', '2024-01-01', '2024-01-15', '우선 처리 요청'),
(2, '비타민 C 함량, 수분 함량, 미생물 시험', 'MEDIUM', '2024-01-02', '2024-01-16', NULL),
(3, '오메가-3 함량, 과산화물가, 산가', 'MEDIUM', '2024-01-03', '2024-01-17', NULL),
(4, '유산균 수, 생존율, 수분 함량', 'LOW', '2024-01-04', '2024-01-18', '냉장 보관 필수'),
(5, '칼슘 마그네슘 함량, 용출 시험', 'MEDIUM', '2024-01-05', '2024-01-19', NULL),
(6, '비타민 함량, 미네랄 함량, 붕해 시험', 'HIGH', '2024-01-06', '2024-01-20', NULL),
(7, '단백질 함량, 아미노산 조성, 수분 함량', 'MEDIUM', '2024-01-07', '2024-01-21', NULL),
(8, '철분 함량, 용출 시험, 붕해 시험', 'URGENT', '2024-01-08', '2024-01-22', '긴급 처리 요청'),
(9, '콜라겐 함량, 분자량 분포, 수분 함량', 'LOW', '2024-01-09', '2024-01-23', NULL),
(10, '루테인 함량, 순도 시험', 'MEDIUM', '2024-01-10', '2024-01-24', NULL),
(11, '글루코사민 함량, 순도 시험', 'HIGH', '2024-01-11', '2024-01-25', NULL),
(12, '코엔자임 Q10 함량, 안정성 시험', 'MEDIUM', '2024-01-12', '2024-01-26', NULL),
(13, '마그네슘 함량, 용출 시험', 'LOW', '2024-01-13', '2024-01-27', NULL),
(14, '비타민 D3 함량, 순도 시험', 'MEDIUM', '2024-01-14', '2024-01-28', NULL),
(15, '셀레늄 함량, 중금속 시험', 'HIGH', '2024-01-15', '2024-01-29', NULL);

-- 시험 접수 정보 샘플 데이터 (접수된 시험들만)
INSERT INTO receipt_info (test_id, received_by, received_date, sample_condition, storage_location, notes) VALUES
(1, 2, '2024-01-02 09:30:00', 'GOOD', 'A-101', '정상 접수'),
(2, 2, '2024-01-03 10:15:00', 'GOOD', 'A-102', '정상 접수'),
(3, 3, '2024-01-04 14:20:00', 'GOOD', 'B-201', '정상 접수'),
(4, 2, '2024-01-05 11:45:00', 'GOOD', 'A-103', '냉장 보관 완료'),
(6, 3, '2024-01-07 08:30:00', 'GOOD', 'B-202', '정상 접수'),
(7, 2, '2024-01-08 13:15:00', 'GOOD', 'A-104', '정상 접수'),
(8, 3, '2024-01-09 09:00:00', 'GOOD', 'B-203', '긴급 처리'),
(9, 2, '2024-01-10 15:30:00', 'GOOD', 'A-105', '정상 접수'),
(11, 3, '2024-01-12 10:00:00', 'GOOD', 'B-204', '정상 접수'),
(12, 2, '2024-01-13 14:45:00', 'GOOD', 'A-106', '정상 접수'),
(13, 3, '2024-01-14 11:30:00', 'GOOD', 'B-205', '정상 접수'),
(14, 2, '2024-01-15 16:00:00', 'GOOD', 'A-107', '정상 접수');

-- 시험 결과 정보 샘플 데이터 (결과 입력 이상 진행된 시험들)
INSERT INTO result_info (test_id, tested_by, test_started_date, test_completed_date, results, pass_fail, comments) VALUES
(1, 6, '2024-01-03 09:00:00', '2024-01-10 17:00:00', '{"성분함량": "99.8%", "순도": "적합", "용출": "95.2%"}', 'PASS', '모든 시험 항목 적합'),
(2, 6, '2024-01-04 10:00:00', NULL, '{"비타민C함량": "진행중", "수분함량": "2.1%", "미생물": "적합"}', 'PENDING', '비타민 C 함량 시험 진행 중'),
(6, 9, '2024-01-08 09:00:00', '2024-01-12 16:30:00', '{"비타민함량": "적합", "미네랄함량": "적합", "붕해": "15분"}', 'PASS', '모든 항목 적합'),
(7, 6, '2024-01-09 11:00:00', NULL, '{"단백질함량": "85.2%", "아미노산": "진행중", "수분": "4.5%"}', 'PENDING', '아미노산 조성 분석 중'),
(11, 9, '2024-01-13 08:30:00', '2024-01-18 14:20:00', '{"글루코사민함량": "98.5%", "순도": "적합"}', 'PASS', '품질 기준 적합'),
(12, 6, '2024-01-14 13:00:00', NULL, '{"코엔자임함량": "100.2%", "안정성": "진행중"}', 'PENDING', '안정성 시험 진행 중');

-- 시험 완료 정보 샘플 데이터 (완료된 시험들만)
INSERT INTO completion_info (test_id, completed_by, completion_date, report_path, final_approval_status, final_comments) VALUES
(1, 2, '2024-01-11 09:00:00', '/reports/2024/01/test_001_report.pdf', 'APPROVED', '최종 승인 완료'),
(6, 3, '2024-01-13 11:00:00', '/reports/2024/01/test_006_report.pdf', 'APPROVED', '최종 승인 완료'),
(11, 2, '2024-01-19 10:30:00', '/reports/2024/01/test_011_report.pdf', 'APPROVED', '최종 승인 완료');

-- 승인 시스템 샘플 데이터
INSERT INTO approvals (status) VALUES
('APPROVED'),
('APPROVED'),
('APPROVED'),
('PENDING'),
('PARTIAL_APPROVED'),
('PENDING');

-- 승인 요청 샘플 데이터
INSERT INTO approval_requests (approval_id, requester_id, comment) VALUES
(1, 4, '시험 결과 최종 승인 요청'),
(2, 4, '시험 완료 승인 요청'),
(3, 5, '글루코사민 시험 승인 요청'),
(4, 5, '비타민 C 시험 승인 요청'),
(5, 7, '단백질 파우더 시험 승인 요청'),
(6, 8, '철분 캡슐 긴급 승인 요청');

-- 승인 서명 샘플 데이터
INSERT INTO approval_signs (approval_id, approver_id, target_id, stage, status, comment) VALUES
(1, 2, 1, 'COMPLETED', 'APPROVED', '최종 승인'),
(1, 3, 1, 'COMPLETED', 'APPROVED', '품질 기준 적합'),
(2, 2, 6, 'COMPLETED', 'APPROVED', '승인 완료'),
(2, 6, 6, 'COMPLETED', 'APPROVED', '시험 결과 적합'),
(3, 3, 11, 'COMPLETED', 'APPROVED', '최종 승인'),
(3, 9, 11, 'COMPLETED', 'APPROVED', '품질 검증 완료'),
(4, 2, 2, 'RESULT_INPUT', 'PENDING', '결과 검토 중'),
(4, 6, 2, 'RESULT_INPUT', 'APPROVED', '시험 결과 적합'),
(5, 3, 7, 'RESULT_INPUT', 'PENDING', '추가 검토 필요'),
(5, 6, 7, 'RESULT_INPUT', 'APPROVED', '1차 승인 완료'),
(6, 2, 8, 'RECEIPT_APPROVAL', 'PENDING', '접수 승인 대기'),
(6, 3, 8, 'RECEIPT_APPROVAL', 'PENDING', '검토 중');

-- 감사 로그 샘플 데이터
INSERT INTO audit_logs (user_id, action, entity_type, entity_id, old_values, new_values, ip_address, user_agent) VALUES
(1, 'CREATE', 'TEST', 1, NULL, '{"product_id": 1, "requester_id": 4, "stage": "REQUEST"}', '192.168.1.100', 'Mozilla/5.0'),
(2, 'UPDATE', 'TEST', 1, '{"stage": "REQUEST"}', '{"stage": "RECEIPT"}', '192.168.1.101', 'Mozilla/5.0'),
(6, 'UPDATE', 'TEST', 1, '{"stage": "RECEIPT"}', '{"stage": "RESULT_INPUT"}', '192.168.1.102', 'Mozilla/5.0'),
(2, 'UPDATE', 'TEST', 1, '{"stage": "RESULT_INPUT"}', '{"stage": "COMPLETED"}', '192.168.1.101', 'Mozilla/5.0'),
(4, 'CREATE', 'TEST', 2, NULL, '{"product_id": 2, "requester_id": 4, "stage": "REQUEST"}', '192.168.1.103', 'Mozilla/5.0'),
(3, 'UPDATE', 'APPROVAL', 1, '{"status": "PENDING"}', '{"status": "APPROVED"}', '192.168.1.104', 'Mozilla/5.0'),
(9, 'CREATE', 'RESULT_INFO', 1, NULL, '{"test_id": 1, "pass_fail": "PASS"}', '192.168.1.105', 'Mozilla/5.0'),
(1, 'UPDATE', 'USER', 4, '{"role": "USER"}', '{"role": "USER"}', '192.168.1.100', 'Mozilla/5.0'),
(2, 'DELETE', 'TEMP_DATA', 999, '{"temp": "data"}', NULL, '192.168.1.101', 'Mozilla/5.0'),
(6, 'CREATE', 'COMPLETION_INFO', 1, NULL, '{"test_id": 1, "final_approval_status": "APPROVED"}', '192.168.1.102', 'Mozilla/5.0');