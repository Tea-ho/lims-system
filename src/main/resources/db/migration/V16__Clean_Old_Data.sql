-- Clean old data with incompatible format
-- Version: 16.0

SET FOREIGN_KEY_CHECKS = 0;

-- receipt_infos 테이블의 기존 데이터 삭제 (호환되지 않는 형식)
DELETE FROM receipt_infos;

-- result_infos 테이블의 기존 데이터 삭제
DELETE FROM result_infos;

-- completion_infos 테이블의 기존 데이터 삭제
DELETE FROM completion_infos;

-- tests 테이블에서 관련 외래키 초기화
UPDATE tests SET receipt_info_id = NULL WHERE receipt_info_id IS NOT NULL;
UPDATE tests SET result_info_id = NULL WHERE result_info_id IS NOT NULL;
UPDATE tests SET completion_info_id = NULL WHERE completion_info_id IS NOT NULL;

SET FOREIGN_KEY_CHECKS = 1;