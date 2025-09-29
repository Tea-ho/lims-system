# 🚀 LIMS 시스템 - 프론트엔드

## 📋 프로젝트 개요

Laboratory Information Management System의 승인 워크플로우를 위한 **토스 스타일 모던 프론트엔드**입니다.

### ✨ 주요 기능

- **🔐 인증 시스템**: JWT 기반 로그인/로그아웃
- **📊 대시보드**: 실시간 승인 통계 및 현황
- **✅ 전원 승인제**: 모든 승인자의 동의 필요
- **⚡ 동시성 제어**: 버전 기반 충돌 방지 및 자동 재시도
- **🎨 토스 스타일 UI**: 모던하고 직관적인 사용자 경험
- **📱 반응형 디자인**: 모바일/태블릿/데스크톱 대응

---

## 🛠️ 기술 스택

### 프레임워크 & 라이브러리
- **React 18** - 최신 React 기능 활용
- **TypeScript** - 타입 안전성 보장
- **Vite** - 빠른 개발 서버 및 빌드
- **React Router Dom** - SPA 라우팅

### 상태 관리 & 데이터
- **React Query** - 서버 상태 관리 및 캐싱
- **Axios** - HTTP 클라이언트
- **React Hook Form** - 폼 상태 관리

### UI & 스타일링
- **Tailwind CSS** - 유틸리티 CSS 프레임워크
- **Framer Motion** - 애니메이션 및 인터랙션
- **Lucide React** - 아이콘 라이브러리
- **React Hot Toast** - 토스트 알림

---

## 🚀 시작하기

### 1. 의존성 설치
```bash
cd frontend
npm install
```

### 2. 환경 변수 설정
```bash
# .env 파일 생성
VITE_API_BASE_URL=http://localhost:8080/api
VITE_WS_URL=ws://localhost:8080/ws
```

### 3. 개발 서버 실행
```bash
npm run dev
```

브라우저에서 http://localhost:3000 접속

### 4. 프로덕션 빌드
```bash
npm run build
```

---

## 📁 프로젝트 구조

```
frontend/
├── src/
│   ├── components/          # 재사용 가능한 UI 컴포넌트
│   ├── pages/              # 페이지 컴포넌트
│   ├── hooks/              # 커스텀 훅
│   ├── services/           # API 서비스
│   ├── types/              # TypeScript 타입 정의
│   └── index.css          # 글로벌 스타일
├── package.json
├── vite.config.ts        # Vite 설정
├── tailwind.config.js    # Tailwind 설정
└── tsconfig.json         # TypeScript 설정
```

---

## 🎨 디자인 시스템

### 토스 스타일 특징
- **둥근 모서리**: 12px (rounded-xl)
- **그림자**: 계층적 shadow 시스템
- **애니메이션**: 부드러운 spring 애니메이션
- **호버 효과**: scale & translate 변환

---

## 🔧 주요 기능 구현

### 1. 전원 승인제 UI
- 진행률 표시 바
- 승인자별 상태 뱃지
- 실시간 승인 현황 업데이트

### 2. 동시성 제어 처리
- 버전 충돌 감지 및 알림
- 자동 재시도 메커니즘
- 사용자 친화적 오류 메시지

### 3. 실시간 업데이트
- React Query를 통한 자동 갱신
- WebSocket 실시간 알림 (예정)
- 낙관적 UI 업데이트

---

## 🧪 테스트 & 배포

### 개발 환경
```bash











npm run dev
```

### 프로덕션 빌드
```bash
npm run build
npm run preview
```

---

## 📈 로드맵

### ✅ Phase 1: 기본 기능 (완료)
- [x] 인증 시스템
- [x] 대시보드
- [x] 승인 워크플로우
- [x] 토스 스타일 UI

### 🚧 Phase 2: 고급 기능
- [ ] 알림 시스템
- [ ] 파일 첨부
- [ ] 승인 템플릿
- [ ] 배치 승인

### 🔮 Phase 3: 확장 기능
- [ ] PWA 지원
- [ ] 모바일 앱
- [ ] 다국어 지원
- [ ] 고급 분석

---