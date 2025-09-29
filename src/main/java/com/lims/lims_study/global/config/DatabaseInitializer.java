package com.lims.lims_study.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class DatabaseInitializer implements ApplicationRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            // products 테이블에 created_at, updated_at 컬럼 추가
            addTimestampColumns();
        } catch (Exception e) {
            System.err.println("데이터베이스 초기화 중 오류 발생: " + e.getMessage());
        }
    }

    private void addTimestampColumns() {
        try {
            // created_at 컬럼 추가 (이미 있다면 무시)
            jdbcTemplate.execute("ALTER TABLE products ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");

            // updated_at 컬럼 추가 (이미 있다면 무시)
            jdbcTemplate.execute("ALTER TABLE products ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");

            // 기존 데이터에 타임스탬프 설정
            int updatedRows = jdbcTemplate.update(
                "UPDATE products SET created_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE created_at IS NULL OR updated_at IS NULL"
            );

            System.out.println("products 테이블에 타임스탬프 컬럼 추가 완료. 업데이트된 행: " + updatedRows);

        } catch (Exception e) {
            System.err.println("타임스탬프 컬럼 추가 중 오류: " + e.getMessage());
        }
    }
}