-- Flyway 마이그레이션 히스토리 확인
SELECT * FROM flyway_schema_history ORDER BY installed_rank DESC LIMIT 10;

-- receipt_infos 테이블 구조 확인
SHOW CREATE TABLE receipt_infos;