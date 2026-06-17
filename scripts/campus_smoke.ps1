<# .SYNOPSIS
Run SmartCampusPortal MVP API smoke checks against a running backend.

.DESCRIPTION
This script does not start MySQL, Redis, or the Spring Boot application.
Start the backend first, then run this script to verify the role-based MVP
campus API surface with demo accounts seeded by the campus SQL scripts.
#>

param(
    [string]$BaseUrl = "http://127.0.0.1:8081",
    [string]$Password = "admin123",
    [switch]$Help
)

$ErrorActionPreference = "Stop"

if ($Help) {
    Write-Host "Usage:"
    Write-Host "  powershell -ExecutionPolicy Bypass -File scripts\campus_smoke.ps1 [-BaseUrl http://127.0.0.1:8081] [-Password admin123]"
    Write-Host ""
    Write-Host "Prerequisites:"
    Write-Host "  1. MySQL and Redis are running."
    Write-Host "  2. Base RuoYi SQL and campus SQL scripts have been imported."
    Write-Host "  3. Backend is running and captcha is disabled for automation smoke."
    exit 0
}

$BaseUrl = $BaseUrl.TrimEnd("/")

function Join-ApiUrl {
    param([string]$Path)
    if ($Path.StartsWith("/")) {
        return "$BaseUrl$Path"
    }
    return "$BaseUrl/$Path"
}

function Assert-Code200 {
    param(
        [object]$Response,
        [string]$Label
    )

    if ($null -eq $Response) {
        throw "$Label returned an empty response"
    }

    if ($Response.PSObject.Properties.Name -contains "code" -and [int]$Response.code -ne 200) {
        $message = ""
        if ($Response.PSObject.Properties.Name -contains "msg") {
            $message = $Response.msg
        }
        throw "$Label failed with code $($Response.code): $message"
    }
}

function Invoke-CampusRequest {
    param(
        [ValidateSet("GET", "POST", "PUT", "DELETE")]
        [string]$Method,
        [string]$Path,
        [string]$Token,
        [object]$Body = $null,
        [string]$Label = $Path
    )

    $headers = @{}
    if ($Token) {
        $headers["Authorization"] = "Bearer $Token"
    }

    $params = @{
        Method = $Method
        Uri = Join-ApiUrl $Path
        Headers = $headers
        TimeoutSec = 15
    }

    if ($null -ne $Body) {
        $params["ContentType"] = "application/json;charset=utf-8"
        $params["Body"] = ($Body | ConvertTo-Json -Depth 8)
    }

    Write-Host "  $Method $Path" -ForegroundColor DarkGray
    $response = Invoke-RestMethod @params
    Assert-Code200 -Response $response -Label $Label
    return $response
}

function Login-DemoUser {
    param(
        [string]$Username,
        [string]$Password
    )

    Write-Host "Login as $Username" -ForegroundColor Cyan
    $body = @{
        username = $Username
        password = $Password
        code = ""
        uuid = ""
    }
    $response = Invoke-CampusRequest -Method "POST" -Path "/login" -Token "" -Body $body -Label "login $Username"
    if (-not ($response.PSObject.Properties.Name -contains "token") -or [string]::IsNullOrWhiteSpace($response.token)) {
        throw "login $Username did not return a token"
    }
    return $response.token
}

function Test-EndpointGroup {
    param(
        [string]$Role,
        [string]$Token,
        [string[]]$Paths
    )

    Write-Host "Checking $Role endpoints" -ForegroundColor Cyan
    foreach ($path in $Paths) {
        Invoke-CampusRequest -Method "GET" -Path $path -Token $Token -Label "$Role $path" | Out-Null
    }
}

$studentPaths = @(
    "/getInfo",
    "/getRouters",
    "/campus/portal/current",
    "/campus/academic/profile/student",
    "/campus/academic/courses/my",
    "/campus/academic/schedule/my",
    "/campus/academic/grades/my",
    "/campus/academic/exams/my",
    "/campus/academic/electives/available",
    "/campus/office/applications/my",
    "/campus/card/account",
    "/campus/card/transactions/my",
    "/campus/payment/items/pending",
    "/campus/payment/records/my",
    "/campus/asset/assets/available",
    "/campus/asset/borrows/my",
    "/campus/student/profile/my",
    "/campus/student/records/my"
)

$teacherPaths = @(
    "/getInfo",
    "/getRouters",
    "/campus/portal/current",
    "/campus/academic/teacher/profile",
    "/campus/academic/teacher/courses",
    "/campus/academic/teacher/schedule",
    "/campus/academic/teacher/exams",
    "/campus/academic/teacher/sections/100/scores",
    "/campus/office/applications/my",
    "/campus/card/account",
    "/campus/card/transactions/my",
    "/campus/asset/assets/available",
    "/campus/asset/borrows/my"
)

$leaderPaths = @(
    "/getInfo",
    "/getRouters",
    "/campus/portal/current",
    "/campus/dashboard/leader",
    "/campus/office/applications/todo",
    "/campus/asset/borrows/todo",
    "/campus/student/overview"
)

Write-Host "===== SmartCampusPortal MVP API Smoke =====" -ForegroundColor Green
Write-Host "Backend: $BaseUrl"

$studentToken = Login-DemoUser -Username "student" -Password $Password
Test-EndpointGroup -Role "student" -Token $studentToken -Paths $studentPaths

$teacherToken = Login-DemoUser -Username "teacher" -Password $Password
Test-EndpointGroup -Role "teacher" -Token $teacherToken -Paths $teacherPaths

$leaderToken = Login-DemoUser -Username "leader" -Password $Password
Test-EndpointGroup -Role "leader" -Token $leaderToken -Paths $leaderPaths

Write-Host "===== MVP API smoke passed =====" -ForegroundColor Green
