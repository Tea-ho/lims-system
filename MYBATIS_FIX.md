# MyBatis 매퍼 오류 해결 가이드

## 🚨 발생한 문제
```
java.lang.ClassNotFoundException: Cannot find class: com.lims.lims_study.domain.testRepository.model.TestStage
Failed to parse mapping resource: TestMapper.xml
```

## ✅ 해결한 문제들

### 1. 잘못된 패키지 경로 수정
**TestMapper.xml**에서 잘못된 클래스 경로 참조:
```xml
<!-- 수정 전 (잘못된 경로) -->
<select id="findByStage" parameterType="com.lims.lims_study.domain.testRepository.model.TestStage">

<!-- 수정 후 (올바른 경로) -->
<select id="findByStage" parameterType="com.lims.lims_study.domain.test.model.TestStage">
```

### 2. MyBatis 타입 별칭 설정
**MyBatisConfig.java** 생성하여 타입 별칭 등록:
- `Approval`, `ApprovalRequest`, `ApprovalSign`
- `Test`, `TestStage`, `User`, `Product`
- `ApprovalStatus` 등

### 3. Enum 타입 핸들러 설정
**application.yml**에 Enum 기본 핸들러 설정:
```yaml
mybatis:
  configuration:
    default-enum-type-handler: org.apache.ibatis.type.EnumTypeHandler
```

### 4. 매퍼 XML에서 명시적 타입 지정
모든 Enum 필드에 `typeHandler` 명시:
```xml
<result property="status" column="status" typeHandler="org.apache.ibatis.type.EnumTypeHandler"/>
<result property="stage" column="stage" typeHandler="org.apache.ibatis.type.EnumTypeHandler"/>
```

### 5. 로깅 패턴 오류 수정
**application.yml**에서 `%wEx` → `%ex` 변경:
```yaml
logging:
  pattern:
    console: "%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} ... %m%n%ex"
```

## 🔧 수정된 파일 목록
- ✅ `TestMapper.xml` - 패키지 경로 수정, Enum 핸들러 추가
- ✅ `ApprovalSignMapper.xml` - Enum 핸들러 추가
- ✅ `MyBatisConfig.java` - 타입 별칭 등록
- ✅ `application.yml` - Enum 핸들러 및 로깅 패턴 수정
- ✅ `logback-spring.xml` - 로깅 패턴 수정

## 🚀 실행 명령
```bash
# 프로젝트 정리
gradlew.bat clean

# 애플리케이션 실행
gradlew.bat bootRun
```

## 📝 추가 개선사항
- MyBatis 매퍼 스캔 경로를 `classpath:mappers/**/*.xml`로 변경
- Spring Security 로그 레벨을 INFO로 조정 (성능 향상)
- 동시성 제어를 위한 `updateWithVersion` 매퍼 추가

이제 모든 MyBatis 관련 오류가 해결되었습니다! 🎉
