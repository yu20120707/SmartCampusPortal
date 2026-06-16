<# .SYNOPSIS
Build Smart Campus Portal — backend (Maven) + frontend (npm)
#>

$ErrorActionPreference = "Stop"
$rootDir = Split-Path -Parent $PSScriptRoot

Write-Host "===== Building Backend (Maven) =====" -ForegroundColor Cyan
Set-Location $rootDir
& mvn clean install -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "Backend build FAILED" -ForegroundColor Red
    exit $LASTEXITCODE
}
Write-Host "Backend build SUCCESS" -ForegroundColor Green

Write-Host "===== Building Frontend (npm) =====" -ForegroundColor Cyan
Set-Location (Join-Path $rootDir "ruoyi-ui")
& npm install
if ($LASTEXITCODE -ne 0) {
    Write-Host "Frontend dependency install FAILED" -ForegroundColor Red
    exit $LASTEXITCODE
}
& npm run build:prod
if ($LASTEXITCODE -ne 0) {
    Write-Host "Frontend build FAILED" -ForegroundColor Red
    exit $LASTEXITCODE
}
Write-Host "Frontend build SUCCESS" -ForegroundColor Green

Set-Location $rootDir

Write-Host "`nTo start backend:" -ForegroundColor Yellow
Write-Host "  mvn spring-boot:run -pl ruoyi-admin"
Write-Host "To start frontend:" -ForegroundColor Yellow
Write-Host "  cd ruoyi-ui && npm run dev"
