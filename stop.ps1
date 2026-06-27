# Detiene el contenedor de base de datos
Write-Host "Deteniendo contenedores..." -ForegroundColor Yellow
docker compose down
Write-Host "Listo." -ForegroundColor Green
