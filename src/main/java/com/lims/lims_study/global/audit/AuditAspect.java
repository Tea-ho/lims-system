package com.lims.lims_study.global.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;
    private final ExpressionParser parser = new SpelExpressionParser();

    @AfterReturning(value = "@annotation(auditable)", returning = "result")
    public void auditMethod(JoinPoint joinPoint, Auditable auditable, Object result) {
        try {
            // SpEL Context 설정
            StandardEvaluationContext context = new StandardEvaluationContext();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String[] parameterNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();

            // 파라미터를 context에 추가
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }
            context.setVariable("result", result);

            // Entity ID 추출
            Long entityId = extractEntityId(auditable, context);

            // User ID 추출
            Long userId = extractUserId(auditable, context);

            // Old Value 추출
            Object oldValue = null;
            if (auditable.recordOldValue() && !auditable.oldValueExpression().isEmpty()) {
                oldValue = parser.parseExpression(auditable.oldValueExpression()).getValue(context);
            }

            // New Value 추출
            Object newValue = null;
            if (auditable.recordNewValue()) {
                if (!auditable.newValueExpression().isEmpty()) {
                    newValue = parser.parseExpression(auditable.newValueExpression()).getValue(context);
                } else {
                    // 기본적으로 result를 new value로 사용
                    newValue = result;
                }
            }

            // Audit Log 생성
            AuditLog auditLog = new AuditLog(
                    auditable.entityType(),
                    entityId,
                    auditable.action(),
                    userId
            );

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

            // 비동기로 저장
            auditLogRepository.insert(auditLog);
            log.debug("✅ Audit log recorded: {} {} for {}:{}",
                    auditable.action(), auditable.entityType(), auditable.entityType(), entityId);

        } catch (Exception e) {
            log.error("❌ Failed to record audit log: {}", e.getMessage(), e);
        }
    }

    private Long extractEntityId(Auditable auditable, StandardEvaluationContext context) {
        if (auditable.entityIdExpression().isEmpty()) {
            return null;
        }

        try {
            Object value = parser.parseExpression(auditable.entityIdExpression()).getValue(context);
            if (value instanceof Long) {
                return (Long) value;
            } else if (value instanceof Number) {
                return ((Number) value).longValue();
            }
        } catch (Exception e) {
            log.warn("Failed to extract entity ID: {}", e.getMessage());
        }
        return null;
    }

    private Long extractUserId(Auditable auditable, StandardEvaluationContext context) {
        // 1. 어노테이션에서 명시한 표현식이 있으면 사용
        if (!auditable.userIdExpression().isEmpty()) {
            try {
                Object value = parser.parseExpression(auditable.userIdExpression()).getValue(context);
                if (value instanceof Long) {
                    return (Long) value;
                } else if (value instanceof Number) {
                    return ((Number) value).longValue();
                }
            } catch (Exception e) {
                log.warn("Failed to extract user ID from expression: {}", e.getMessage());
            }
        }

        // 2. SecurityContext에서 가져오기
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()
                    && !"anonymousUser".equals(authentication.getPrincipal())) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof Long) {
                    return (Long) principal;
                } else if (principal instanceof Number) {
                    return ((Number) principal).longValue();
                }
                // UserDetails 등에서 ID 추출 로직 추가 가능
            }
        } catch (Exception e) {
            log.warn("Failed to extract user ID from SecurityContext: {}", e.getMessage());
        }

        return null;
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
