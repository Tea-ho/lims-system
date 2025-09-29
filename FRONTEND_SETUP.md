# 프론트엔드 실행 가이드

## 📁 디렉토리 이동
먼저 프론트엔드 디렉토리로 이동하세요:

```bash
cd frontend
```

## 📦 의존성 설치
Node.js 패키지들을 설치합니다:

```bash
npm install
```

## 🚀 개발 서버 실행
개발 서버를 시작합니다:

```bash
npm run dev
```

## 🌐 접속
브라우저에서 다음 주소로 접속:
- **프론트엔드**: http://localhost:3000
- **백엔드**: http://localhost:8083

## ⚠️ 문제 해결

### 1. Node.js 버전 확인
```bash
node --version  # v16 이상 필요
npm --version   # v7 이상 필요
```

### 2. 패키지 설치 오류 시
```bash
# 캐시 정리 후 재설치
npm cache clean --force
rm -rf node_modules package-lock.json
npm install
```

### 3. 포트 충돌 시
다른 포트로 실행:
```bash
npm run dev -- --port 3001
```

## 📋 전체 실행 순서

1. **백엔드 실행** (포트 8083)
   ```bash
   # 루트 디렉토리에서
   gradlew.bat bootRun
   ```

2. **프론트엔드 실행** (포트 3000)
   ```bash
   # frontend 디렉토리에서
   cd frontend
   npm install
   npm run dev
   ```

성공하면 토스 스타일의 LIMS 시스템이 실행됩니다!
