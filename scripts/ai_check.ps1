<# .SYNOPSIS
Check (build + test) Smart Campus Portal
#>

$ErrorActionPreference = "Stop"
$rootDir = Split-Path -Parent $PSScriptRoot
Set-Location $rootDir

Write-Host "===== Step 1: Build Backend =====" -ForegroundColor Cyan
& mvn clean install -DskipTests

Write-Host "===== Step 2: Run Backend Tests =====" -ForegroundColor Cyan
& mvn test
if ($LASTEXITCODE -ne 0) {
    Write-Host "Backend tests FAILED" -ForegroundColor Red
    exit $LASTEXITCODE
}

Write-Host "===== Step 3: Build Frontend =====" -ForegroundColor Cyan
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
Set-Location $rootDir

Write-Host "===== All checks passed =====" -ForegroundColor Green
