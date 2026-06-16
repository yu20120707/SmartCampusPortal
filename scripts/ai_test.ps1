<# .SYNOPSIS
Test Smart Campus Portal — backend (Maven)
#>

$ErrorActionPreference = "Stop"
$rootDir = Split-Path -Parent $PSScriptRoot

Write-Host "===== Running Backend Tests (Maven) =====" -ForegroundColor Cyan
Set-Location $rootDir
& mvn test
if ($LASTEXITCODE -ne 0) {
    Write-Host "Tests FAILED" -ForegroundColor Red
    exit $LASTEXITCODE
}
Write-Host "===== Tests complete =====" -ForegroundColor Green
