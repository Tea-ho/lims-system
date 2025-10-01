package com.lims.lims_study.global.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 메서드에 감사 로그를 기록하기 위한 어노테이션
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {

    /**
     * 엔티티 타입 (예: "TEST", "USER", "PRODUCT")
     */
    String entityType();

    /**
     * 수행되는 액션 (예: "CREATE", "UPDATE", "DELETE")
     */
    String action();

    /**
     * 엔티티 ID를 가져올 파라미터 이름 (SpEL 표현식 지원)
     * 예: "testId", "#test.id", "#result.id"
     */
    String entityIdExpression() default "";

    /**
     * 사용자 ID를 가져올 파라미터 이름 (SpEL 표현식 지원)
     * 예: "userId", "#test.userId"
     * 비어있으면 SecurityContext에서 가져옴
     */
    String userIdExpression() default "";

    /**
     * 변경 전 값을 기록할지 여부
     */
    boolean recordOldValue() default false;

    /**
     * 변경 전 값을 가져올 표현식 (SpEL)
     */
    String oldValueExpression() default "";

    /**
     * 변경 후 값을 기록할지 여부
     */
    boolean recordNewValue() default true;

    /**
     * 변경 후 값을 가져올 표현식 (SpEL)
     */
    String newValueExpression() default "";
}
