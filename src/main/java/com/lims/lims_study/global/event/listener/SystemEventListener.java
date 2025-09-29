package com.lims.lims_study.global.event.listener;

import com.lims.lims_study.global.event.user.UserCreatedEvent;
import com.lims.lims_study.global.event.test.TestStageChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SystemEventListener {

    @Async
    @EventListener
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        log.info("사용자 생성 이벤트 처리 - User ID: {}, Username: {}, Authorities: {}", 
                event.getUserId(), event.getUsername(), event.getAuthorities());
        
        // 사용자 생성 후 처리 로직
        // 예: 환영 이메일 발송, 기본 설정 생성, 감사 로그 기록 등
        sendWelcomeEmail(event.getUsername());
        createDefaultUserSettings(event.getUserId());
        recordAuditLog("USER_CREATED", event.getUserId(), event.getUsername());
    }

    @Async
    @EventListener
    public void handleTestStageChangedEvent(TestStageChangedEvent event) {
        log.info("시험 단계 변경 이벤트 처리 - Test ID: {}, {} -> {}", 
                event.getTestId(), event.getPreviousStage(), event.getCurrentStage());
        
        // 시험 단계 변경 후 처리 로직
        // 예: 알림 발송, 작업 할당, 리포트 생성 등
        sendStageChangeNotification(event);
        assignTasksBasedOnStage(event.getTestId(), event.getCurrentStage());
        recordAuditLog("TEST_STAGE_CHANGED", event.getTestId(), 
                       String.format("%s -> %s", event.getPreviousStage(), event.getCurrentStage()));
    }

    private void sendWelcomeEmail(String username) {
        // 환영 이메일 발송 로직
        log.debug("환영 이메일 발송: {}", username);
    }

    private void createDefaultUserSettings(Long userId) {
        // 기본 사용자 설정 생성 로직
        log.debug("기본 사용자 설정 생성: {}", userId);
    }

    private void sendStageChangeNotification(TestStageChangedEvent event) {
        // 단계 변경 알림 발송 로직
        log.debug("단계 변경 알림 발송: Test ID {}", event.getTestId());
    }

    private void assignTasksBasedOnStage(Long testId, String stage) {
        // 단계별 작업 할당 로직
        log.debug("작업 할당: Test ID {}, Stage {}", testId, stage);
    }

    private void recordAuditLog(String action, Long entityId, String details) {
        // 감사 로그 기록 로직
        log.info("감사 로그 기록 - Action: {}, Entity ID: {}, Details: {}", action, entityId, details);
    }
}
