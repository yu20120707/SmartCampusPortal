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

Write-Host "===== All checks passed =====" -ForegroundColor Green
