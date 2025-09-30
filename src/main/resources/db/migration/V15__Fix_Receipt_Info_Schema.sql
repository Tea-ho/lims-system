-- Fix info tables schema
-- Version: 15.0

SET FOREIGN_KEY_CHECKS = 0;

-- 1. request_infos 테이블 수정
ALTER TABLE request_infos
DROP COLUMN IF EXISTS is_urgent;

ALTER TABLE request_infos
ADD COLUMN IF NOT EXISTS requires_approval BOOLEAN DEFAULT FALSE;

-- 2. receipt_infos 테이블 수정
ALTER TABLE receipt_infos
DROP COLUMN IF EXISTS receipt_date,
DROP COLUMN IF EXISTS responsible_person,
DROP COLUMN IF EXISTS notes;

ALTER TABLE receipt_infos
ADD COLUMN IF NOT EXISTS receipt_details TEXT,
ADD COLUMN IF NOT EXISTS requires_approval BOOLEAN DEFAULT FALSE,
ADD COLUMN IF NOT EXISTS approval_id BIGINT;

CREATE INDEX IF NOT EXISTS idx_receipt_approval_id ON receipt_infos(approval_id);
CREATE INDEX IF NOT EXISTS idx_receipt_requires_approval ON receipt_infos(requires_approval);

-- 3. result_infos 테이블 수정
ALTER TABLE result_infos
DROP COLUMN IF EXISTS test_result,
DROP COLUMN IF EXISTS conclusion,
DROP COLUMN IF EXISTS test_date;

ALTER TABLE result_infos
ADD COLUMN IF NOT EXISTS result_data TEXT,
ADD COLUMN IF NOT EXISTS requires_approval BOOLEAN DEFAULT FALSE,
ADD COLUMN IF NOT EXISTS approval_id BIGINT;

CREATE INDEX IF NOT EXISTS idx_result_approval_id ON result_infos(approval_id);
CREATE INDEX IF NOT EXISTS idx_result_requires_approval ON result_infos(requires_approval);

-- 4. completion_infos 테이블 수정
ALTER TABLE completion_infos
DROP COLUMN IF EXISTS completion_date,
DROP COLUMN IF EXISTS final_report,
DROP COLUMN IF EXISTS approved_by;

ALTER TABLE completion_infos
ADD COLUMN IF NOT EXISTS completion_notes TEXT,
ADD COLUMN IF NOT EXISTS requires_approval BOOLEAN DEFAULT FALSE,
ADD COLUMN IF NOT EXISTS approval_id BIGINT;

CREATE INDEX IF NOT EXISTS idx_completion_approval_id ON completion_infos(approval_id);
CREATE INDEX IF NOT EXISTS idx_completion_requires_approval ON completion_infos(requires_approval);

SET FOREIGN_KEY_CHECKS = 1;