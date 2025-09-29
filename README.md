# LIMS Study Application

실험실 정보 관리 시스템(Laboratory Information Management System) 학습용 프로젝트입니다.

## 📋 프로젝트 개요

이 프로젝트는 실험실에서 사용하는 시험 관리 시스템의 핵심 기능을 구현한 학습용 애플리케이션입니다. Spring Boot를 기반으로 하며, 클린 아키텍처와 DDD(Domain Driven Design) 패턴을 적용하여 개발되었습니다.

## 🛠 기술 스택

- **Backend Framework**: Spring Boot 2.7.18
- **Language**: Java 17
- **Build Tool**: Gradle 7.x
- **Database**: MySQL 8.0
- **ORM**: MyBatis
- **Security**: Spring Security + JWT
- **View Template**: JSP
- **Testing**: JUnit 5, Spring Boot Test
- **Documentation**: AsciiDoc, Spring REST Docs
- **Code Quality**: JaCoCo

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
- 사용자 등록, 수정, 삭제, 조회
- JWT 기반 인증 시스템
- 권한별 접근 제어

### 2. 제품 관리
- 제품 정보 CRUD
- 제품 검색 기능

### 3. 시험 관리
시험의 전체 생명주기를 단계별로 관리합니다:

```
의뢰(REQUEST) → 접수(RECEIPT) → 접수결재(RECEIPT_APPROVAL) → 결과입력(RESULT_INPUT) → 완료(COMPLETED)
```

### 4. 결재 시스템
- 결재 요청 생성 및 처리
- 결재선 관리
- 승인/반려 처리

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

### 3. 애플리케이션 설정
`src/main/resources/application-local.yml` 파일에서 데이터베이스 연결 정보를 확인하고 필요시 수정합니다.

### 4. 애플리케이션 실행
```bash
# 개발 모드로 실행
./gradlew runLocal

# 또는 일반 실행
./gradlew bootRun
```

애플리케이션이 성공적으로 시작되면 http://localhost:8083 에서 접근할 수 있습니다.

## 🧪 테스트

### 전체 테스트 실행
```bash
./gradlew test
```

### 테스트 커버리지 확인
```bash
./gradlew jacocoTestReport
```
커버리지 리포트는 `build/reports/jacoco/test/html/index.html`에서 확인할 수 있습니다.

### 코드 품질 검사
```bash
./gradlew checkCodeQuality
```

## 📚 API 문서

애플리케이션 실행 후 다음 경로에서 API 문서를 확인할 수 있습니다:
- REST Docs: http://localhost:8083/docs/index.html (빌드 후 생성)

### 주요 API 엔드포인트

#### 인증
- `POST /api/auth/login` - 로그인
- `POST /api/auth/logout` - 로그아웃

#### 사용자 관리
- `GET /api/users` - 사용자 목록 조회
- `POST /api/users` - 사용자 생성
- `GET /api/users/{id}` - 사용자 상세 조회
- `PUT /api/users/{id}` - 사용자 정보 수정
- `DELETE /api/users/{id}` - 사용자 삭제

#### 제품 관리
- `GET /api/products` - 제품 목록 조회
- `POST /api/products` - 제품 생성
- `GET /api/products/{id}` - 제품 상세 조회
- `PUT /api/products/{id}` - 제품 정보 수정
- `DELETE /api/products/{id}` - 제품 삭제

#### 시험 관리
- `GET /api/tests` - 시험 목록 조회
- `POST /api/tests` - 시험 생성
- `GET /api/tests/{id}` - 시험 상세 조회
- `PUT /api/tests/{id}` - 시험 정보 수정
- `DELETE /api/tests/{id}` - 시험 삭제
- `POST /api/tests/{id}/receipt` - 시험 접수
- `POST /api/tests/{id}/receipt-approval` - 접수 결재

## 🔧 개발 도구

### 빌드 태스크
```bash
# 개발용 실행
./gradlew dev

# 운영용 빌드
./gradlew prodBuild

# 문서 생성
./gradlew docs

# 테스트 리포트 생성
./gradlew testReport
```

### 코드 포맷팅
프로젝트는 일관된 코드 스타일을 유지하기 위해 다음 규칙을 따릅니다:
- Java: Google Java Style Guide
- 들여쓰기: 4 spaces
- 라인 길이: 120자

## 📊 모니터링

Spring Boot Actuator를 통해 애플리케이션 상태를 모니터링할 수 있습니다:
- Health Check: http://localhost:8083/actuator/health
- Metrics: http://localhost:8083/actuator/metrics
- Info: http://localhost:8083/actuator/info

## 🔒 보안

### JWT 토큰
- 토큰 유효기간: 24시간
- 알고리즘: HS512
- 헤더: `Authorization: Bearer <token>`

### 권한
- `ROLE_USER`: 일반 사용자
- `ROLE_ADMIN`: 관리자
- `ROLE_MANAGER`: 매니저

## 📝 로깅

로그는 다음 위치에 저장됩니다:
- 일반 로그: `logs/lims_study.log`
- 에러 로그: `logs/lims_study-error.log`
- SQL 로그: `logs/lims_study-sql.log`

로그 레벨:
- 개발 환경: DEBUG
- 운영 환경: INFO

## 🤝 기여

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📜 라이선스

이 프로젝트는 MIT 라이선스를 따릅니다. 자세한 내용은 `LICENSE` 파일을 참조하세요.

## 📞 연락처

프로젝트 관련 문의사항이 있으시면 이슈를 등록해 주세요.

## 🙏 감사의 말

이 프로젝트는 학습 목적으로 제작되었으며, 실제 운영 환경에서 사용하기 위해서는 추가적인 보안 강화 및 성능 최적화가 필요합니다.
