package com.lims.lims_study.infrastructure.config;

import com.universallogger.UniversalLogger;
import com.universallogger.sql.SqlCaptureProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.File;

/**
 * UniversalLogger 설정
 * - Spring Boot AutoConfiguration과 함께 작동
 * - 설정 파일 초기화 및 DataSource wrapping 담당
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UniversalLoggerConfig implements BeanPostProcessor {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${universal-logger.log-path}")
    private String logBasePath;

    @Value("${universal-logger.config-path}")
    private String configPath;

    @Value("${universal-logger.sql-capture.enabled}")
    private boolean sqlCaptureEnabled;

    private boolean dataSourceWrapped = false;

    @PostConstruct
    public void initUniversalLogger() {
        try {
            // 프로젝트별 로그 디렉토리 생성
            String projectName = applicationName.replaceAll("[^a-zA-Z0-9_-]", "_");
            String logPath = logBasePath.endsWith("/") ? logBasePath + projectName + "/" : logBasePath + "/" + projectName + "/";

            File logDir = new File(logPath);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            // classpath에서 설정 파일 로드
            ClassPathResource resource = new ClassPathResource(configPath);

            if (resource.exists()) {
                File configFile = resource.getFile();
                UniversalLogger.init(configFile.getAbsolutePath());
                UniversalLogger.info("✅ UniversalLogger initialized for " + projectName);
                UniversalLogger.info("📁 Log directory: " + logPath);
            } else {
                System.err.println("⚠️ UniversalLogger config file not found: " + configPath);
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize UniversalLogger: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSource && !dataSourceWrapped && sqlCaptureEnabled) {
            if (bean instanceof SqlCaptureProxy) {
                return bean;
            }

            dataSourceWrapped = true;
            UniversalLogger.info("🔍 SQL logging enabled - DataSource wrapped");
            return new SqlCaptureProxy((DataSource) bean);
        }
        return bean;
    }
}
