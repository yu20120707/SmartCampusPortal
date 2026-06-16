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

Write-Host "`nTo start backend:" -ForegroundColor Yellow
Write-Host "  mvn spring-boot:run -pl ruoyi-admin"
Write-Host "To start frontend:" -ForegroundColor Yellow
Write-Host "  cd ruoyi-ui && npm run dev"
