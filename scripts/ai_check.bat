@echo off
REM Check (build + test) for Smart Campus Portal (Windows)

echo ===== Step 1: Build Backend =====
call mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo Build FAILED
    exit /b %errorlevel%
)

echo ===== Step 2: Run Backend Tests =====
call mvn test
if %errorlevel% neq 0 (
    echo Tests FAILED
    exit /b %errorlevel%
)

echo ===== All checks passed =====
