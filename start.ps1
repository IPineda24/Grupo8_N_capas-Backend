# =============================================
# Script de inicio - Telemedicina Backend
# Uso: click derecho -> "Run with PowerShell"
#      o desde terminal: .\start.ps1
# =============================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Telemedicina Backend - Inicio        " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# 1. Verificar que Docker Desktop este corriendo
Write-Host "`n[1/3] Verificando Docker Desktop..." -ForegroundColor Yellow
$dockerRunning = $false
try {
    docker info 2>&1 | Out-Null
    $dockerRunning = $LASTEXITCODE -eq 0
} catch {}

if (-not $dockerRunning) {
    Write-Host "  Docker Desktop no esta corriendo. Iniciandolo..." -ForegroundColor Red
    Start-Process "C:\Program Files\Docker\Docker\Docker Desktop.exe" -ErrorAction SilentlyContinue
    Write-Host "  Esperando que Docker inicie (30 segundos)..." -ForegroundColor Yellow
    Start-Sleep -Seconds 30
    # Reintentar
    docker info 2>&1 | Out-Null
    if ($LASTEXITCODE -ne 0) {
        Write-Host "  ERROR: Docker no pudo iniciar. Abrelo manualmente y vuelve a correr este script." -ForegroundColor Red
        Read-Host "Presiona Enter para salir"
        exit 1
    }
}
Write-Host "  Docker esta corriendo OK" -ForegroundColor Green

# 2. Levantar el contenedor de la base de datos
Write-Host "`n[2/3] Levantando base de datos PostgreSQL..." -ForegroundColor Yellow
docker compose up db -d
if ($LASTEXITCODE -ne 0) {
    Write-Host "  ERROR: No se pudo levantar la base de datos." -ForegroundColor Red
    Read-Host "Presiona Enter para salir"
    exit 1
}

# Esperar a que PostgreSQL este listo
Write-Host "  Esperando que PostgreSQL este listo..." -ForegroundColor Yellow
$retries = 0
do {
    Start-Sleep -Seconds 3
    $retries++
    $status = docker inspect --format="{{.State.Health.Status}}" telemedicina-db 2>&1
    Write-Host "  Estado: $status (intento $retries/10)" -ForegroundColor Gray
} while ($status -ne "healthy" -and $retries -lt 10)

if ($status -ne "healthy") {
    Write-Host "  ADVERTENCIA: PostgreSQL puede no estar listo, pero intentando de todas formas..." -ForegroundColor Yellow
} else {
    Write-Host "  Base de datos lista OK" -ForegroundColor Green
}

# 3. Iniciar la aplicacion Spring Boot
Write-Host "`n[3/3] Iniciando Spring Boot en http://localhost:8080 ..." -ForegroundColor Yellow
Write-Host "  Presiona Ctrl+C para detener la aplicacion`n" -ForegroundColor Gray
.\gradlew.bat bootRun
