# LIMS Study Application

![Java](https://img.shields.io/badge/Java-11-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Test Coverage](https://img.shields.io/badge/Tests-56%20passed-brightgreen)

실험실 정보 관리 시스템(Laboratory Information Management System) 학습용 프로젝트입니다.

## 📋 프로젝트 개요

이 프로젝트는 실험실에서 사용하는 시험 관리 시스템의 핵심 기능을 구현한 학습용 애플리케이션입니다. Spring Boot를 기반으로 하며, 클린 아키텍처와 DDD(Domain Driven Design) 패턴을 적용하여 개발되었습니다.

## 🛠 기술 스택

### Backend
- **Framework**: Spring Boot 2.7.18
- **Language**: Java 11
- **Build Tool**: Gradle 8.x
- **Database**: MySQL 8.0
- **Migration**: Flyway
- **ORM**: MyBatis + Spring Data JPA
- **Security**: Spring Security + JWT (HS512)
- **Validation**: Spring Validation
- **AOP**: Spring AOP (Audit Logging)

### View & API
- **View Template**: JSP
- **API Documentation**: AsciiDoc
- **API Specification**: Swagger/OpenAPI 3.0

### Testing & Quality
- **Testing Framework**: JUnit 5, Spring Boot Test
- **Mocking**: Mockito
- **Code Coverage**: JaCoCo
- **Test Types**: Unit Tests, Integration Tests, Controller Tests (56 tests)

### DevOps & Monitoring
- **Caching**: Caffeine Cache
- **Monitoring**: Spring Boot Actuator
- **Logging**: Logback + SLF4J
- **Event Processing**: Spring Events

## 🏗 아키텍처

```
src/main/java/com/lims/lims_study/
├── application/          # 애플리케이션 서비스 계층
├── domain/              # 도메인 계층
├── global/              # 전역 설정 및 공통 기능
└── presentation/        # 프레젠테이션 계층 (REST API)
```

### 계층별 역할

- **Presentation Layer**: REST API 컨트롤러, 요청/응답 처리
- **Application Layer**: 비즈니스 로직 조율, 트랜잭션 관리
- **Domain Layer**: 핵심 비즈니스 로직, 도메인 모델
- **Infrastructure Layer**: 데이터베이스, 외부 시스템 연동

## 🚀 주요 기능

### 1. 사용자 관리
- ✅ 사용자 CRUD (등록, 조회, 수정, 삭제)
- ✅ JWT 기반 인증/인가 시스템
- ✅ 역할 기반 접근 제어 (ROLE_USER, ROLE_MANAGER, ROLE_ADMIN)
- ✅ 비밀번호 암호화 (BCrypt)
- ✅ 비밀번호 정책 검증 (대소문자, 숫자, 특수문자 포함)
- ✅ 사용자 검색 및 페이징

### 2. 제품 관리
- ✅ 제품 정보 CRUD
- ✅ 제품 검색 및 필터링
- ✅ 제품별 시험 이력 조회

### 3. 시험 관리
시험의 전체 생명주기를 7단계로 관리합니다:

```
접수(RECEIPT) → 접수승인(RECEIPT_APPROVAL) → 분석(ANALYSIS) → 분석승인(ANALYSIS_APPROVAL)
→ 결과(RESULT) → 결과승인(RESULT_APPROVAL) → 완료(COMPLETE)
```

- ✅ 시험 CRUD 및 단계별 상태 관리
- ✅ 시험 검색 (제품, 요청자, 단계별)
- ✅ 시험 단계 변경 및 이력 관리
- ✅ 목표 완료일 설정 및 추적

### 4. 승인 시스템 (전원 승인제)
- ✅ 접수/분석/결과 단계별 승인 요청
- ✅ 다중 승인자 지정 (ROLE_ADMIN, ROLE_MANAGER)
- ✅ 전원 승인제: 모든 승인자가 승인해야 최종 승인
- ✅ 승인/거부 처리 및 코멘트 관리
- ✅ 승인 거부 시 자동 단계 롤백
- ✅ Optimistic Locking을 통한 동시성 제어
- ✅ 승인 대기 목록 조회 (페이징)

### 5. 대시보드 통계
- ✅ 전체 시험/제품/사용자 통계
- ✅ 단계별 시험 수 집계
- ✅ 완료율 및 대기 중인 승인 수
- ✅ 실시간 DB 데이터 기반 통계

### 6. 감사 로그 (Audit)
- ✅ AOP 기반 자동 감사 로그
- ✅ 생성/수정 사용자 및 일시 자동 기록
- ✅ 변경 이력 추적

## ⚙️ 설치 및 실행

### 사전 요구사항
- Java 11 이상
- MySQL 8.0
- Gradle 7.x

### 1. 저장소 클론
```bash
git clone <repository-url>
cd LIMS_STUDY
```

### 2. 데이터베이스 설정
```sql
CREATE DATABASE lims_study;
CREATE USER 'lims_study'@'localhost' IDENTIFIED BY 'rlaxogh123!';
GRANT ALL PRIVILEGES ON lims_study.* TO 'lims_study'@'localhost';
```

### 3. 데이터베이스 마이그레이션
```bash
# Flyway 마이그레이션 실행
./gradlew flywayMigrate
```

### 4. 애플리케이션 설정
`src/main/resources/application-local.yml` 파일에서 데이터베이스 연결 정보를 확인하고 필요시 수정합니다.

### 5. 애플리케이션 실행
```bash
# 개발 모드로 실행 (테스트 포함)
./gradlew bootRun

# 또는 로컬 프로파일로 실행
./gradlew runLocal
```

애플리케이션이 성공적으로 시작되면 http://localhost:8083 에서 접근할 수 있습니다.

### 6. 빌드 및 배포
```bash
# JAR 파일 생성 (테스트 + API 문서 포함)
./gradlew bootJar

# 생성된 JAR 실행
java -jar build/libs/lims-study-0.0.1-SNAPSHOT.jar
```

## 🧪 테스트

### CI/CD 테스트 자동화
이 프로젝트는 완전한 테스트 자동화를 구축했습니다:
- ✅ `bootJar` 실행 시 자동으로 전체 테스트 실행
- ✅ `bootRun` 실행 시 자동으로 테스트 실행 후 구동
- ✅ 테스트 실패 시 빌드 중단

### 테스트 통계
- **총 테스트**: 56개
- **Controller 테스트**: 40개
  - UserControllerTest: 7개
  - ProductControllerTest: 7개
  - TestControllerTest: 7개
  - ApprovalControllerTest: 7개
  - DashboardControllerTest: 1개
  - AuthControllerTest: 2개
  - 기타: 9개
- **Service 테스트**: 16개
  - UserDomainServiceTest: 4개
  - ProductDomainServiceTest: 4개
  - TestStateDomainServiceTest: 4개
  - ApprovalDomainServiceTest: 4개

### 테스트 실행 명령어
```bash
# 전체 테스트 실행
./gradlew test

# 테스트 결과 리포트 확인
# build/reports/tests/test/index.html

# 테스트 커버리지 확인
./gradlew jacocoTestReport
# build/reports/jacoco/test/html/index.html

# 코드 품질 검사
./gradlew checkCodeQuality
```

### 테스트 특징
- **Spring Security 제외**: 모든 Controller 테스트에서 Security Auto-configuration 제외
- **MockMvc**: Spring MVC 테스트 프레임워크 사용
- **Mockito**: 의존성 모킹
- **@WebMvcTest**: 웹 계층만 테스트 (경량 테스트)

## 📚 API 문서

### 문서 접근 방법
애플리케이션 실행 후 다음 경로에서 API 문서를 확인할 수 있습니다:
- **AsciiDoc API 문서**: http://localhost:8083/docs/index.html
- **Swagger UI**: http://localhost:8083/swagger-ui.html

JAR 파일 빌드 시 API 문서가 자동으로 생성되어 `/static/docs/` 경로에 포함됩니다.

### API 문서 특징
- ✅ 전체 API 엔드포인트 목록
- ✅ Request/Response 필드 상세 설명
- ✅ 파라미터 타입, 제약조건, 최대 길이 명시
- ✅ HTTP Status Code 설명
- ✅ 예제 요청/응답 (일부 API)

### 주요 API 엔드포인트

#### 인증 (Authentication)
- `POST /api/auth/login` - 로그인 (JWT 토큰 발급)
- `POST /api/auth/logout` - 로그아웃

#### 사용자 관리 (User)
- `POST /api/users` - 사용자 생성
- `GET /api/users/{id}` - 사용자 조회
- `GET /api/users/me` - 현재 사용자 조회
- `GET /api/users` - 사용자 검색 (페이징)
- `PUT /api/users/{id}` - 사용자 수정
- `DELETE /api/users/{id}` - 사용자 삭제
- `GET /api/users/exists?username={username}` - 사용자명 존재 여부 확인

#### 제품 관리 (Product)
- `POST /api/products` - 제품 생성
- `GET /api/products/{id}` - 제품 조회
- `GET /api/products/all` - 전체 제품 조회
- `GET /api/products` - 제품 검색 (페이징)
- `PUT /api/products/{id}` - 제품 수정
- `DELETE /api/products/{id}` - 제품 삭제

#### 시험 관리 (Test)
- `POST /api/tests` - 시험 생성
- `GET /api/tests/{id}` - 시험 조회
- `GET /api/tests` - 시험 검색 (제품, 요청자, 단계별)
- `PUT /api/tests/{id}` - 시험 수정
- `PUT /api/tests/{id}/stage` - 시험 단계 변경
- `DELETE /api/tests/{id}` - 시험 삭제

#### 승인 관리 (Approval)
- `POST /api/approvals` - 승인 요청 생성
- `GET /api/approvals/{id}` - 승인 조회
- `GET /api/approvals/target/{targetId}` - 타겟별 승인 목록 조회
- `GET /api/approvals/pending` - 대기 중인 승인 목록 조회 (페이징)
- `PUT /api/approvals/{approvalId}/signs/{signId}` - 승인 서명 업데이트
- `PUT /api/approvals/{id}/process` - 승인 처리 (승인/거부)
- `DELETE /api/approvals/{id}` - 승인 삭제

#### 대시보드 (Dashboard)
- `GET /api/dashboard/stats` - 통계 조회

## 🔧 개발 도구

### Gradle 태스크
```bash
# 애플리케이션 실행
./gradlew bootRun                    # 테스트 후 실행
./gradlew runLocal                   # 로컬 프로파일로 실행

# 빌드
./gradlew bootJar                    # JAR 파일 생성 (테스트 + 문서 포함)
./gradlew clean bootJar              # 클린 빌드

# 테스트
./gradlew test                       # 전체 테스트 실행
./gradlew test --tests *UserControllerTest  # 특정 테스트만 실행

# 문서
./gradlew asciidoctor                # API 문서 생성 (build/docs/html5/)

# 코드 품질
./gradlew jacocoTestReport           # 테스트 커버리지 리포트
./gradlew checkCodeQuality           # 코드 품질 검사

# 데이터베이스
./gradlew flywayMigrate              # DB 마이그레이션
./gradlew flywayClean                # DB 초기화 (주의!)
./gradlew flywayInfo                 # 마이그레이션 정보 조회
```

### 프로젝트 구조
```
LIMS_STUDY/
├── src/
│   ├── main/
│   │   ├── java/com/lims/lims_study/
│   │   │   ├── application/          # 애플리케이션 서비스 계층
│   │   │   ├── domain/              # 도메인 계층 (비즈니스 로직)
│   │   │   ├── global/              # 전역 설정 및 공통 기능
│   │   │   └── presentation/        # 프레젠테이션 계층 (REST API)
│   │   ├── resources/
│   │   │   ├── db/migration/        # Flyway 마이그레이션 스크립트
│   │   │   ├── mapper/              # MyBatis Mapper XML
│   │   │   └── application*.yml     # 애플리케이션 설정
│   │   └── webapp/WEB-INF/views/    # JSP 뷰 템플릿
│   ├── test/java/                   # 테스트 코드 (56개)
│   └── docs/asciidoc/               # API 문서 (AsciiDoc)
└── build/
    ├── libs/                        # 빌드된 JAR 파일
    ├── reports/
    │   ├── tests/                   # 테스트 결과 리포트
    │   └── jacoco/                  # 커버리지 리포트
    └── docs/html5/                  # 생성된 API 문서
```

### 코드 컨벤션
- **패키지 구조**: 계층별 분리 (presentation, application, domain)
- **명명 규칙**:
  - Controller: `*Controller.java`
  - Service: `*ApplicationService.java` (Application Layer), `*DomainService.java` (Domain Layer)
  - Repository: `*Repository.java`
  - DTO: `*Dto.java`, `*ResponseDto.java`
  - Test: `*Test.java`
- **들여쓰기**: 4 spaces
- **라인 길이**: 120자

## 📊 모니터링

Spring Boot Actuator를 통해 애플리케이션 상태를 모니터링할 수 있습니다:
- Health Check: http://localhost:8083/actuator/health
- Metrics: http://localhost:8083/actuator/metrics
- Info: http://localhost:8083/actuator/info

## 🔒 보안

### JWT 인증
- **알고리즘**: HS512
- **토큰 유효기간**: 24시간
- **헤더 형식**: `Authorization: Bearer <token>`
- **비밀번호 암호화**: BCrypt
- **비밀번호 정책**: 8-100자, 대소문자+숫자+특수문자 포함

### 권한 체계
- `ROLE_USER`: 일반 사용자 (시험 의뢰, 조회)
- `ROLE_MANAGER`: 매니저 (승인 권한)
- `ROLE_ADMIN`: 관리자 (전체 권한, 사용자 관리)

### 보안 기능
- ✅ CSRF 보호
- ✅ SQL Injection 방지 (MyBatis PreparedStatement)
- ✅ XSS 방지 (Spring Security)
- ✅ 비밀번호 평문 저장 금지
- ✅ JWT 토큰 기반 무상태(Stateless) 인증

## 📝 로깅 및 감사

### 로그 파일
로그는 다음 위치에 저장됩니다:
- 일반 로그: `logs/lims_study.log`
- 에러 로그: `logs/lims_study-error.log`
- SQL 로그: 콘솔 출력 (개발 환경)

### 로그 레벨
- **개발 환경**: DEBUG
- **운영 환경**: INFO

### 감사 로그 (Audit)
AOP를 통해 자동으로 다음 정보를 기록합니다:
- 생성일시 (`created_at`)
- 생성자 (`created_by`)
- 수정일시 (`updated_at`)
- 수정자 (`updated_by`)

## 🎯 데이터베이스

### 스키마
- **데이터베이스**: lims_study
- **문자셋**: utf8mb4
- **Collation**: utf8mb4_unicode_ci
- **엔진**: InnoDB

### 주요 테이블
- `users`: 사용자 정보
- `products`: 제품 정보
- `tests`: 시험 정보
- `approvals`: 승인 요청
- `approval_signs`: 승인 서명 (다대다 관계)

### 마이그레이션
Flyway를 사용한 버전 관리:
- 마이그레이션 스크립트: `src/main/resources/db/migration/`
- 명명 규칙: `V{version}__{description}.sql`
- 자동 실행: 애플리케이션 시작 시 (운영 환경에서는 수동 실행 권장)

## 🚦 CI/CD

### 현재 구축된 CI/CD
- ✅ **자동 테스트**: `bootJar`, `bootRun` 실행 시 자동으로 전체 테스트 실행
- ✅ **자동 문서화**: 빌드 시 AsciiDoc API 문서 자동 생성 및 JAR 포함
- ✅ **테스트 리포트**: JUnit + JaCoCo 리포트 자동 생성
- ✅ **빌드 실패 방지**: 테스트 실패 시 빌드 중단

### 향후 확장 가능 사항
- GitHub Actions / Jenkins를 통한 자동 빌드
- Docker 컨테이너화
- Blue-Green 배포
- 통합 테스트 자동화

## 🎓 학습 목표 및 적용 기술

이 프로젝트를 통해 다음 기술과 패턴을 학습할 수 있습니다:

### 아키텍처 패턴
- ✅ **Clean Architecture**: Presentation - Application - Domain - Infrastructure 계층 분리
- ✅ **DDD (Domain-Driven Design)**: 도메인 중심 설계
- ✅ **CQRS**: Command와 Query 분리

### 디자인 패턴
- ✅ **Repository Pattern**: 데이터 접근 추상화
- ✅ **Service Layer Pattern**: 비즈니스 로직 분리
- ✅ **DTO Pattern**: 계층 간 데이터 전송
- ✅ **Builder Pattern**: 객체 생성 (일부)
- ✅ **Strategy Pattern**: 승인 전략

### 개발 원칙
- ✅ **SOLID 원칙** 적용
- ✅ **의존성 역전 (DIP)**
- ✅ **단일 책임 원칙 (SRP)**
- ✅ **개방-폐쇄 원칙 (OCP)**

### 테스트
- ✅ **단위 테스트**: Service 계층 테스트
- ✅ **통합 테스트**: Controller 테스트
- ✅ **Mocking**: Mockito를 활용한 의존성 모킹
- ✅ **테스트 커버리지**: JaCoCo

### 기타
- ✅ **AOP**: 횡단 관심사 분리 (감사 로그)
- ✅ **Event-Driven**: Spring Events를 활용한 이벤트 처리
- ✅ **Caching**: Caffeine 캐시
- ✅ **DB Migration**: Flyway
- ✅ **API 문서화**: AsciiDoc + Swagger

## 🤝 기여

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📜 라이선스

이 프로젝트는 학습 목적으로 제작되었습니다.

## 📞 연락처

프로젝트 관련 문의사항이 있으시면 이슈를 등록해 주세요.

## 🙏 주의사항

이 프로젝트는 **학습 목적**으로 제작되었습니다. 실제 운영 환경에서 사용하기 위해서는 다음 사항을 추가로 고려해야 합니다:

- ⚠️ 보안 강화 (JWT Secret 키 환경변수화, HTTPS 적용 등)
- ⚠️ 성능 최적화 (DB 인덱스, 쿼리 최적화, 커넥션 풀 튜닝 등)
- ⚠️ 에러 핸들링 강화
- ⚠️ 로깅 및 모니터링 시스템 구축
- ⚠️ 백업 및 복구 전략
- ⚠️ 부하 테스트 및 성능 테스트

---

**Built with ❤️ for Learning**
