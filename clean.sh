#!/bin/bash

echo "🧹 LIMS 프로젝트 빌드 캐시 정리 중..."

# Gradle 캐시 정리
echo "1. Gradle clean 실행..."
./gradlew clean

# 빌드 디렉토리 정리
echo "2. 빌드 아티팩트 제거..."
rm -rf build/
rm -rf out/
rm -rf .gradle/

# 로그 디렉토리 정리
echo "3. 로그 파일 정리..."
rm -rf logs/

echo "✅ 캐시 정리 완료!"
echo ""
echo "🚀 이제 다음 명령으로 실행하세요:"
echo "./gradlew bootRun"
