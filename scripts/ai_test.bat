@echo off
REM Test script for Smart Campus Portal (Windows)

echo ===== Running Backend Tests (Maven) =====
call mvn test
if %errorlevel% neq 0 (
    echo Tests FAILED
    exit /b %errorlevel%
)
echo ===== Tests complete =====
