package com.lims.lims_study.global.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {
    
    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    @Async
    public void recordAuditLog(String entityType, Long entityId, String action, Long userId, Object oldValue, Object newValue) {
        try {
            AuditLog auditLog = new AuditLog(entityType, entityId, action, userId);
            
            if (oldValue != null) {
                auditLog.setOldValues(objectMapper.writeValueAsString(oldValue));
            }
            
            if (newValue != null) {
                auditLog.setNewValues(objectMapper.writeValueAsString(newValue));
            }
            
            // HTTP 요청 정보 수집
            HttpServletRequest request = getCurrentHttpRequest();
            if (request != null) {
                auditLog.setIpAddress(getClientIpAddress(request));
                auditLog.setUserAgent(request.getHeader("User-Agent"));
            }
            
            auditLogRepository.insert(auditLog);
            log.debug("감사 로그 기록: {} {} for entity {}:{}", action, entityType, entityType, entityId);
            
        } catch (Exception e) {
            log.error("감사 로그 기록 실패: {}", e.getMessage(), e);
        }
    }

    @Async
    public void recordLoginAttempt(String username, boolean successful, String ipAddress, String userAgent) {
        try {
            AuditLog auditLog = new AuditLog("USER", null, successful ? "LOGIN_SUCCESS" : "LOGIN_FAILED", null);
            auditLog.setNewValues(String.format("{\"username\":\"%s\"}", username));
            auditLog.setIpAddress(ipAddress);
            auditLog.setUserAgent(userAgent);
            
            auditLogRepository.insert(auditLog);
            log.info("로그인 시도 기록: {} - {}", username, successful ? "성공" : "실패");
            
        } catch (Exception e) {
            log.error("로그인 감사 로그 기록 실패: {}", e.getMessage(), e);
        }
    }

    @Async
    public void recordSecurityEvent(String eventType, String details, Long userId, String ipAddress) {
        try {
            AuditLog auditLog = new AuditLog("SECURITY", null, eventType, userId);
            auditLog.setNewValues(details);
            auditLog.setIpAddress(ipAddress);
            
            auditLogRepository.insert(auditLog);
            log.warn("보안 이벤트 기록: {} - {}", eventType, details);
            
        } catch (Exception e) {
            log.error("보안 감사 로그 기록 실패: {}", e.getMessage(), e);
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
    
    private HttpServletRequest getCurrentHttpRequest() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return attrs.getRequest();
        } catch (Exception e) {
            return null;
        }
    }
}
