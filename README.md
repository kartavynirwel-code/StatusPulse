# ⚡ StatusPulse — Uptime & Incident Monitoring System

**StatusPulse** is a lightweight, high-performance monorepo application for monitoring microservice uptime, tracking incident timelines, and dispatching real-time email alerts on service status changes.

---

## 🏗️ Repository Architecture

```text
StatusPulse/
├── monitor-service/   # Java 21 + Spring Boot 3.x + Maven + MySQL
├── alert-service/     # Python 3.12 + FastAPI + SMTP / Async Background Tasks
├── frontend/          # React 18 + Vite + Glassmorphism UI
├── .gitignore         # Root gitignore rules
└── README.md          # Project overview & startup guide
```

---

## 🧩 Services Overview

### 1. `monitor-service` (Java 21 / Spring Boot 3.x)
- **Entities**:
  - `MonitoredService` (`id`, `name`, `url`, `intervalSeconds`, `createdAt`)
  - `StatusCheck` (`id`, `serviceId`, `status` [UP/DOWN], `responseTimeMs`, `checkedAt`)
- **Key Capabilities**:
  - Full REST CRUD for monitored service targets.
  - Background `@Scheduled` health check engine that pings URLs at specified intervals.
  - Detects status transitions (`UP` ➔ `DOWN`, `DOWN` ➔ `UP`) and triggers POST webhooks to `alert-service`.
  - Calculates uptime percentages and response latency history.

### 2. `alert-service` (Python / FastAPI)
- **Key Capabilities**:
  - `POST /alert` webhook endpoint accepting `{serviceName, status, timestamp}`.
  - Offloads email notifications to a non-blocking FastAPI `BackgroundTask`.
  - Supports real SMTP email dispatch or console log mock mode (`SMTP_MOCK_MODE=true`).

### 3. `frontend` (React / Vite)
- **Key Capabilities**:
  - Live uptime dashboard with status badges, pulse animations, uptime percentage, and ping latency.
  - Incident timeline view showing check history and transitions.
  - Interactive modal form to add/remove services.
  - Auto-refreshes every 5 seconds.
  - Configurable API base URL via `VITE_API_BASE_URL`.

---

## ⚡ Quick Start Guide (Local Development)

### 1. Start `alert-service` (Port 8000)
```bash
cd alert-service
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
export ALERT_SERVICE_PORT=8000
export SMTP_MOCK_MODE=true
uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
```

### 2. Start `monitor-service` (Port 8080)
```bash
cd monitor-service
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=statuspulse
export DB_USER=root
export DB_PASSWORD=secretpassword
export ALERT_SERVICE_URL=http://localhost:8000/alert
export SERVER_PORT=8080

mvn spring-boot:run
```

### 3. Start `frontend` (Port 3000)
```bash
cd frontend
npm install
export VITE_API_BASE_URL=http://localhost:8080
npm run dev
```

Open `http://localhost:3000` in your browser.

---

## 🔑 Environment Variables Reference

Each service contains a `.env.example` file detailing required parameters:
- `monitor-service/.env.example`
- `alert-service/.env.example`
- `frontend/.env.example`

No secrets or hardcoded endpoints exist in the source code.
