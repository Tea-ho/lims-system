package com.lims.lims_study.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
//@Component  // 비활성화: 스키마가 안정화되어 더 이상 자동 수정이 필요없음
@RequiredArgsConstructor
public class DatabaseSchemaFixer implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) {
        try {
            log.info("Checking and fixing database schema...");

            // Foreign key 체크 비활성화
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");

            // receipt_infos 테이블 재생성
            jdbcTemplate.execute("DROP TABLE IF EXISTS receipt_infos");
            jdbcTemplate.execute(
                "CREATE TABLE receipt_infos (" +
                "    id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "    receipt_details TEXT," +
                "    requires_approval BOOLEAN DEFAULT FALSE," +
                "    approval_id BIGINT," +
                "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                "    INDEX idx_approval_id (approval_id)" +
                ")"
            );

            // result_infos 테이블 재생성
            jdbcTemplate.execute("DROP TABLE IF EXISTS result_infos");
            jdbcTemplate.execute(
                "CREATE TABLE result_infos (" +
                "    id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "    result_data TEXT," +
                "    requires_approval BOOLEAN DEFAULT FALSE," +
                "    approval_id BIGINT," +
                "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                "    INDEX idx_approval_id (approval_id)" +
                ")"
            );

            // completion_infos 테이블 재생성
            jdbcTemplate.execute("DROP TABLE IF EXISTS completion_infos");
            jdbcTemplate.execute(
                "CREATE TABLE completion_infos (" +
                "    id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "    completion_notes TEXT," +
                "    requires_approval BOOLEAN DEFAULT FALSE," +
                "    approval_id BIGINT," +
                "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                "    INDEX idx_approval_id (approval_id)" +
                ")"
            );

            // request_infos 테이블에 컬럼 추가 (이미 있으면 무시)
            try {
                jdbcTemplate.execute("ALTER TABLE request_infos ADD COLUMN requires_approval BOOLEAN DEFAULT FALSE");
            } catch (Exception e) {
                log.debug("requires_approval column already exists in request_infos");
            }

            // tests 테이블 외래키 NULL 처리
            jdbcTemplate.execute("UPDATE tests SET receipt_info_id = NULL WHERE receipt_info_id IS NOT NULL");
            jdbcTemplate.execute("UPDATE tests SET result_info_id = NULL WHERE result_info_id IS NOT NULL");
            jdbcTemplate.execute("UPDATE tests SET completion_info_id = NULL WHERE completion_info_id IS NOT NULL");

            // Foreign key 체크 재활성화
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");

            log.info("✅ Database schema fixed successfully!");

        } catch (Exception e) {
            log.error("❌ Failed to fix database schema", e);
        }
    }
}