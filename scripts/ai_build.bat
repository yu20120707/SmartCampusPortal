@echo off
REM Build script for Smart Campus Portal (Windows)
REM Backend: Maven, Frontend: npm

echo ===== Building Backend =====
call mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo Backend build FAILED
    exit /b %errorlevel%
)

echo.
echo ===== Building Frontend =====
pushd ruoyi-ui
call npm install
if %errorlevel% neq 0 (
    popd
    echo Frontend dependency install FAILED
    exit /b %errorlevel%
)
call npm run build:prod
if %errorlevel% neq 0 (
    popd
    echo Frontend build FAILED
    exit /b %errorlevel%
)
popd

echo.
echo ===== Build SUCCESS =====

echo.
echo To start backend: mvn spring-boot:run -pl ruoyi-admin
echo To start frontend: cd ruoyi-ui ^&^& npm run dev
