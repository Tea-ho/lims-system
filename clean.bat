@echo off
echo 🧹 LIMS 프로젝트 빌드 캐시 정리 중...

REM Gradle 캐시 정리
echo 1. Gradle clean 실행...
call gradlew.bat clean

REM 빌드 디렉토리 정리
echo 2. 빌드 아티팩트 제거...
if exist "build" rmdir /s /q "build"
if exist "out" rmdir /s /q "out"
if exist ".gradle" rmdir /s /q ".gradle"

REM 로그 디렉토리 정리
echo 3. 로그 파일 정리...
if exist "logs" rmdir /s /q "logs"

echo ✅ 캐시 정리 완료!
echo.
echo 🚀 이제 다음 명령으로 실행하세요:
echo gradlew.bat bootRun
pause
