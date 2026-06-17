@echo off
REM Run SmartCampusPortal MVP API smoke checks against a running backend.

powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0campus_smoke.ps1" %*
exit /b %errorlevel%
