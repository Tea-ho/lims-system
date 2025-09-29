-- 데이터베이스 완전 초기화
-- Version: 0.0

-- 모든 테이블 삭제 (외래키 제약 조건 무시)
SET FOREIGN_KEY_CHECKS = 0;

-- Flyway 히스토리 테이블 삭제
DROP TABLE IF EXISTS flyway_schema_history;

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