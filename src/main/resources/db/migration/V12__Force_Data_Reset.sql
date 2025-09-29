-- Force data reset and test data insertion
-- Version: 12.0
-- 기존 데이터를 삭제하고 새로운 테스트 데이터 삽입

-- 기존 모든 테이블 데이터 삭제 (TRUNCATE는 외래키 때문에 순서 중요)
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE audit_logs;
TRUNCATE TABLE tests;
TRUNCATE TABLE approval_signs;
TRUNCATE TABLE approval_requests;
TRUNCATE TABLE approvals;
TRUNCATE TABLE completion_infos;
TRUNCATE TABLE result_infos;
TRUNCATE TABLE receipt_infos;
TRUNCATE TABLE request_infos;
TRUNCATE TABLE products;
TRUNCATE TABLE users;

SET FOREIGN_KEY_CHECKS = 1;

-- 기초 데이터 삽입
-- 사용자 데이터
INSERT INTO users (username, password, role) VALUES
('admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'ROLE_ADMIN'),
('user1', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'ROLE_USER'),
('user2', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'ROLE_USER'),
('user3', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'ROLE_USER');

-- 제품 데이터 (정확히 5개만)
INSERT INTO products (name, description, created_at, updated_at) VALUES
('제품 A', '첫 번째 테스트 제품입니다.', NOW(), NOW()),
('제품 B', '두 번째 테스트 제품입니다.', NOW(), NOW()),
('제품 C', '세 번째 테스트 제품입니다.', NOW(), NOW()),
('제품 D', '네 번째 테스트 제품입니다.', NOW(), NOW()),
('제품 E', '다섯 번째 테스트 제품입니다.', NOW(), NOW());

-- 승인 데이터 (대시보드용)
INSERT INTO approvals (status, created_at, updated_at) VALUES
('PENDING', NOW(), NOW()),
('PENDING', NOW(), NOW()),
('PENDING', NOW(), NOW()),
('APPROVED', NOW(), NOW()),
('APPROVED', NOW(), NOW()),
('REJECTED', NOW(), NOW());

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