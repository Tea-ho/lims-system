-- LIMS 시스템 실제 작동용 기초 데이터 삽입
-- Version: 4.0
-- 알려진 패스워드로 사용자 생성

-- 기존 데이터 정리
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE audit_logs;
TRUNCATE TABLE approval_signs;
TRUNCATE TABLE approval_requests;
TRUNCATE TABLE approvals;
TRUNCATE TABLE tests;
TRUNCATE TABLE completion_info;
TRUNCATE TABLE result_info;
TRUNCATE TABLE receipt_info;
TRUNCATE TABLE request_info;
TRUNCATE TABLE products;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- 1. 사용자 데이터 삽입 (알려진 패스워드: admin123, user123 등)
INSERT INTO users (username, password, authorities, created_at, updated_at) VALUES
-- admin / admin123 -> BCrypt hash for "admin123"
('admin', '$2a$10$GRLdNijSQMUvl/au9ofL.eDDiQDuaD7fSKBKT2MmGOjTBFj5H2.6K', 'ADMIN', '2024-01-01 09:00:00', '2024-01-01 09:00:00'),
-- user / user123 -> BCrypt hash for "user123"
('user', '$2a$10$VEjxo0jq2YdICFRnKZr.g.vKqLsq9/9BUC4f2VZd8b8MQ8CYfEQCC', 'USER', '2024-01-01 09:30:00', '2024-01-15 14:20:00'),
-- manager / manager123 -> BCrypt hash for "manager123"
('manager', '$2a$10$7EqJtq98hPqEX/fmvlz/N.L4u4OLVqO8K6QclN8JQBKlhvM8T5RZ.', 'MANAGER', '2024-01-01 10:30:00', '2024-01-14 11:15:00'),
-- analyst / analyst123 -> BCrypt hash for "analyst123"
('analyst', '$2a$10$CnDBOHdepWQZvDyJbJy/c.F8/v8kXZJjXfWclIEHGz4MQNe24eJ7G', 'ANALYST', '2024-01-01 10:00:00', '2024-01-14 16:30:00');

-- 2. 제품 데이터 삽입
INSERT INTO products (name, description, created_at, updated_at) VALUES
('ABC-123 화합물', '신약 후보물질로서 항염 효과가 있는 화합물입니다. 임상 1상 준비중.', '2024-01-01 00:00:00', '2024-01-15 09:30:00'),
('XYZ-456 원료', '주요 원료물질로 사용되는 고순도 화학물질입니다. 순도 99.5% 이상 보장.', '2024-01-02 00:00:00', '2024-01-14 16:20:00'),
('DEF-789 완제품', '최종 의약품으로 출시 예정인 완제품입니다. FDA 승인 대기중.', '2024-01-03 00:00:00', '2024-01-13 15:45:00'),
('GHI-101 중간체', '합성 과정에서 생성되는 중간체 물질입니다. 반응 온도 60-80도.', '2024-01-04 00:00:00', '2024-01-12 10:15:00'),
('JKL-202 첨가제', '제품의 안정성을 향상시키는 첨가제입니다. 보존기간 연장 효과.', '2024-01-05 00:00:00', '2024-01-11 14:30:00');

-- 3. 요청 정보 데이터
INSERT INTO request_info (request_details, request_date, priority, created_at, updated_at) VALUES
('신제품 품질검사가 필요합니다. 긴급 검사 요청드립니다.', '2024-01-15', 'URGENT', '2024-01-15 09:00:00', '2024-01-15 09:00:00'),
('원료 성분 분석을 요청합니다. 순도 99% 이상 확인 필요합니다.', '2024-01-14', 'HIGH', '2024-01-14 14:30:00', '2024-01-14 14:30:00'),
('최종 제품 안정성 검사를 진행해 주세요.', '2024-01-10', 'NORMAL', '2024-01-10 11:15:00', '2024-01-10 11:15:00');

-- 4. 시험 데이터 (간단한 예시)
INSERT INTO tests (product_id, requester_id, request_info_id, stage, created_at, updated_at) VALUES
(1, 2, 1, 'REQUEST', '2024-01-15 09:15:00', '2024-01-15 09:15:00'),
(2, 2, 2, 'RECEIPT', '2024-01-14 14:45:00', '2024-01-14 15:30:00'),
(3, 4, 3, 'COMPLETED', '2024-01-10 11:30:00', '2024-01-12 16:00:00');