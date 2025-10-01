# RESULT_APPROVAL 스테이지 추가 - 수동 마이그레이션 가이드

## 문제
`approval_signs` 테이블의 `stage` 컬럼이 `RESULT_APPROVAL` 값을 지원하지 않아 오류가 발생합니다.

## 해결 방법

### 방법 1: 애플리케이션 재시작 (권장)
1. Spring Boot 애플리케이션을 완전히 종료합니다
2. 애플리케이션을 다시 시작합니다
3. Flyway가 자동으로 V17 마이그레이션을 실행합니다

### 방법 2: 수동 SQL 실행

#### MySQL Workbench 또는 MySQL 클라이언트 사용:

```sql
-- 1. tests 테이블 업데이트
ALTER TABLE tests
MODIFY COLUMN stage ENUM('REQUEST', 'RECEIPT', 'RECEIPT_APPROVAL', 'RESULT_INPUT', 'RESULT_APPROVAL', 'COMPLETED') NOT NULL DEFAULT 'REQUEST';

-- 2. approval_signs 테이블 업데이트
ALTER TABLE approval_signs
MODIFY COLUMN stage ENUM('REQUEST', 'RECEIPT', 'RECEIPT_APPROVAL', 'RESULT_INPUT', 'RESULT_APPROVAL', 'COMPLETED') NOT NULL;
```

#### 명령줄에서 실행:

```bash
# Windows (CMD)
mysql -u lims_user -plims1234 lims_db < update_stage_enum.sql

# 또는 배치 파일 실행
run_migration.bat
```

## 실행 후 확인

다음 SQL로 변경사항을 확인하세요:

```sql
-- tests 테이블 확인
SHOW COLUMNS FROM tests LIKE 'stage';

-- approval_signs 테이블 확인
SHOW COLUMNS FROM approval_signs LIKE 'stage';
```

두 테이블 모두 `RESULT_APPROVAL`이 포함된 ENUM 값을 보여야 합니다.

## 참고
- 파일 위치: `src/main/resources/db/migration/V17__Add_Result_Approval_Stage.sql`
- SQL 스크립트: `update_stage_enum.sql`
- 배치 파일: `run_migration.bat`
