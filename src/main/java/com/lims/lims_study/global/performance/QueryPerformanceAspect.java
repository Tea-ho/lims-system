package com.lims.lims_study.global.performance;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class QueryPerformanceAspect {

    @Around("execution(* com.lims.lims_study.domain.*.repository.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            
            String methodName = joinPoint.getSignature().getName();
            String className = joinPoint.getTarget().getClass().getSimpleName();
            
            if (executionTime > 1000) { // 1초 이상 걸리는 쿼리 경고
                log.warn("Slow Query Detected - {}.{} executed in {} ms", 
                        className, methodName, executionTime);
            } else if (executionTime > 500) { // 0.5초 이상 정보 로그
                log.info("Query Performance - {}.{} executed in {} ms", 
                        className, methodName, executionTime);
            } else {
                log.debug("Query Performance - {}.{} executed in {} ms", 
                        className, methodName, executionTime);
            }
            
            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            
            log.error("Query Error - {}.{} failed after {} ms: {}", 
                    joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().getName(),
                    executionTime,
                    e.getMessage());
            throw e;
        }
    }
}
