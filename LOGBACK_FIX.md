# LIMS 시스템 - 로깅 오류 해결 가이드

## 🚨 발생한 문제
```
ERROR in ch.qos.logback.core.pattern.parser.Compiler - There is no conversion class registered for conversion word [wEx]
ERROR in ch.qos.logback.core.pattern.parser.Compiler - [wEx] is not a valid conversion word
```

## ✅ 해결 방법

### 1. 잘못된 Logback 패턴 수정
**문제**: `logback-spring.xml`에서 `%wEx` 패턴 사용
**해결**: `%ex`로 변경

```xml
<!-- 수정 전 (잘못된 패턴) -->
<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n%wEx</pattern>

<!-- 수정 후 (올바른 패턴) -->
<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n%ex</pattern>
```

### 2. 올바른 Logback 변환 패턴들
- `%ex` - 예외 스택 트레이스 (전체)
- `%ex{short}` - 짧은 예외 스택 트레이스
- `%ex{full}` - 전체 예외 스택 트레이스 (기본값과 동일)
- `%rEx` - 루트 원인 예외 강조 (Spring Boot 2.3+에서 지원)
- `%throwable` - %ex와 동일

### 3. 추가 최적화 사항
- Spring Security 로그 레벨을 DEBUG → INFO로 변경 (불필요한 로그 감소)
- 루트 로그 레벨을 DEBUG → INFO로 변경 (성능 향상)
- 동시성 제어 관련 로그는 INFO 레벨 유지

## 🔧 해결 후 실행 명령
```bash
# 프로젝트 정리
./gradlew clean

# 다시 빌드 및 실행
./gradlew bootRun
```

## 📝 참고사항
- Logback 설정 변경 후에는 반드시 애플리케이션 재시작 필요
- 개발 환경에서는 CONSOLE + FILE 로깅
- 프로덕션 환경에서는 FILE 로깅만 사용
- 에러 로그는 별도 파일로 관리
