$ErrorActionPreference = "Stop"

function Exists { param([string]$name) (Get-Command $name -ErrorAction SilentlyContinue) -ne $null }

$machinePath = [System.Environment]::GetEnvironmentVariable('Path','Machine')
$userPath = [System.Environment]::GetEnvironmentVariable('Path','User')
$env:Path = "$machinePath;$userPath"

if (-not (Exists "mkcert")) {
    if (-not (Exists "choco")) { throw "Chocolatey is not available in this shell. Open a new Administrator PowerShell after installing Chocolatey." }
    choco install mkcert -y
    if (-not (Exists "mkcert")) { throw "mkcert installation failed. Check C:\ProgramData\chocolatey\logs\chocolatey.log" }
}

$certDir = Join-Path (Split-Path $PSScriptRoot -Parent) "certs"
New-Item -ItemType Directory -Force -Path $certDir | Out-Null

mkcert -install

Push-Location $certDir
mkcert -key-file dev-key.pem -cert-file dev-cert.pem "api.arcathoria.dev" "game.arcathoria.dev" "localhost" 127.0.0.1 ::1
Pop-Location

Write-Host "OK"
