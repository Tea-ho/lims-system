-- products 테이블에 created_at, updated_at 컬럼 추가 (H2 데이터베이스용)

-- created_at 컬럼 추가 (이미 있다면 오류 무시)
ALTER TABLE products ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- updated_at 컬럼 추가 (이미 있다면 오류 무시)
ALTER TABLE products ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- 기존 데이터에 타임스탬프 설정
UPDATE products
SET created_at = CURRENT_TIMESTAMP,
    updated_at = CURRENT_TIMESTAMP
WHERE created_at IS NULL OR updated_at IS NULL;