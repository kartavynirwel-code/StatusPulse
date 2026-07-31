# StatusPulse - Alert Service

The `alert-service` is a lightweight Python FastAPI microservice responsible for receiving status change alert webhooks from `monitor-service` and firing async email notifications via SMTP (or logging in mock mode).

---

## 🛠️ Required Environment Variables

Copy `.env.example` to `.env` or set environment variables before running:

| Variable | Description | Default Value |
| :--- | :--- | :--- |
| `ALERT_SERVICE_HOST` | Binding Host | `0.0.0.0` |
| `ALERT_SERVICE_PORT` | Port for Alert Service | `8000` |
| `SMTP_HOST` | SMTP Mail Server Host | `smtp.gmail.com` |
| `SMTP_PORT` | SMTP Mail Server Port | `587` |
| `SMTP_USER` | SMTP Authentication User | `alerts@example.com` |
| `SMTP_PASS` | SMTP Authentication Password | `secretpassword` |
| `SMTP_FROM_EMAIL` | Sender Email Address | `alerts@example.com` |
| `ALERT_RECIPIENT_EMAIL` | Incident Alert Recipient | `admin@example.com` |
| `SMTP_MOCK_MODE` | Enable console logging mock (`true`/`false`) | `true` |

---

## 🚀 How to Run Locally

### Step 1: Create Virtual Environment & Install Dependencies
```bash
cd alert-service
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

### Step 2: Set Environment Variables & Start Server
```bash
export ALERT_SERVICE_PORT=8000
export SMTP_MOCK_MODE=true

uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
```

---

## 📡 API Endpoint

### `POST /alert`
Receives status transition alerts from `monitor-service`.

**Request Body:**
```json
{
  "serviceName": "Payment Gateway API",
  "status": "DOWN",
  "timestamp": "2026-07-31T03:26:38Z"
}
```

**Response (HTTP 202 Accepted):**
```json
{
  "message": "Alert received and queued for dispatch",
  "serviceName": "Payment Gateway API",
  "status": "DOWN",
  "status_code": 202
}
```
