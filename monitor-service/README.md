# StatusPulse - Monitor Service

The `monitor-service` is a Spring Boot 3 microservice written in Java 21. It manages monitored services, runs background scheduled health checks (@Scheduled), records uptime history in MySQL, and triggers webhook alerts to `alert-service` on status transitions (`UP` <-> `DOWN`).

---

## 🛠️ Required Environment Variables

Copy `.env.example` to `.env` or set the following environment variables before running:

| Variable | Description | Default Value |
| :--- | :--- | :--- |
| `DB_HOST` | MySQL Host | `localhost` |
| `DB_PORT` | MySQL Port | `3306` |
| `DB_NAME` | MySQL Database Name | `statuspulse` |
| `DB_USER` | MySQL Username | `root` |
| `DB_PASSWORD` | MySQL Password | `secretpassword` |
| `ALERT_SERVICE_URL` | Webhook URL for `alert-service` | `http://localhost:8000/alert` |
| `SERVER_PORT` | Port for Monitor Service | `8080` |

---

## 🚀 How to Run Locally

### Prerequisites
- **Java 21 JDK** installed
- **Apache Maven 3.8+** installed
- Running **MySQL server** (or updated `application.yml` for H2 fallback)

### Step 1: Set Environment Variables
```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=statuspulse
export DB_USER=root
export DB_PASSWORD=your_password
export ALERT_SERVICE_URL=http://localhost:8000/alert
export SERVER_PORT=8080
```

### Step 2: Build & Run
```bash
mvn clean package -DskipTests
java -jar target/monitor-service-0.0.1-SNAPSHOT.jar
```
Or directly using Maven:
```bash
mvn spring-boot:run
```

---

## 📡 REST API Endpoints

- `POST /api/services` - Register a new monitored service
- `GET /api/services` - List all registered services
- `GET /api/services/{id}` - Fetch service details by ID
- `PUT /api/services/{id}` - Update service details
- `DELETE /api/services/{id}` - Delete service & clear its check history
- `GET /api/services/status/current` - Get current status summary (uptime %, response time) for all services
- `GET /api/services/{id}/history` - Get status check history for a service
- `GET /api/services/history/recent` - Get recent global status checks for incident timeline
