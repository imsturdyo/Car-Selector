
# folders
$BackendDir  = "backend"
$FrontendDir = "frontend"

# quick checks
if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) { Write-Error "Maven not found. Install Maven."; exit 1 }
if (-not (Get-Command node -ErrorAction SilentlyContinue)) { Write-Error "Node.js not found. Install Node.js LTS."; exit 1 }
if (-not (Get-Command npm -ErrorAction SilentlyContinue))  { Write-Error "npm not found."; exit 1 }

# start backend
Start-Process -WorkingDirectory $BackendDir -FilePath "cmd.exe" `
  -ArgumentList "/k","mvn -DskipTests spring-boot:run"

# start frontend (install deps once, then dev; falls back to start)
$feCmd = "if not exist node_modules ( (if exist package-lock.json (npm ci) else (npm install)) ) && (npm run dev || npm start)"
Start-Process -WorkingDirectory $FrontendDir -FilePath "cmd.exe" `
  -ArgumentList "/k",$feCmd

Write-Host "Backend  → http://localhost:8080"
Write-Host "Frontend → http://localhost:5173 run 'npm run dev' if modules are already installed"
