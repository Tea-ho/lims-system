-- Add created_at and updated_at columns to products table
-- Version: 14.0

-- products 테이블에 created_at, updated_at 컬럼 추가
ALTER TABLE products
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- 기존 데이터의 created_at, updated_at 값을 현재 시간으로 설정
UPDATE products
SET created_at = CURRENT_TIMESTAMP,
    updated_at = CURRENT_TIMESTAMP
WHERE created_at IS NULL OR updated_at IS NULL;