-- Fix product data schema issue
-- Version: 13.0
-- V12 마이그레이션에서 products 테이블 데이터 삽입 시 created_at, updated_at 컬럼이 누락된 문제를 수정

-- 기존 제품 데이터 삭제
DELETE FROM products;

-- 올바른 스키마로 제품 데이터 재삽입
INSERT INTO products (name, description, created_at, updated_at) VALUES
('아스피린 정제', '해열, 진통, 소염 효과가 있는 의약품', NOW(), NOW()),
('비타민 C 캡슐', '면역력 강화를 위한 비타민 C 보충제', NOW(), NOW()),
('오메가-3 소프트겔', '심혈관 건강을 위한 오메가-3 지방산 보충제', NOW(), NOW()),
('유산균 분말', '장내 미생물 균형을 위한 프로바이오틱스', NOW(), NOW()),
('칼슘 마그네슘 정제', '뼈 건강을 위한 칼슘과 마그네슘 복합제', NOW(), NOW());