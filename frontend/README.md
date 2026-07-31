# StatusPulse - React Frontend

The StatusPulse frontend is built using **React** with **Vite** for fast, modern development and lightweight production builds. It provides a real-time dashboard displaying service uptime cards, live response times, uptime percentages, an incident timeline log, and an interactive form to register or remove monitored services.

---

## ⚙️ Technology Stack Choice: React + Vite

We chose **Vite** over Create React App because:
1. **Lightning-fast Dev Server**: Instant HMR (Hot Module Replacement) with native ES module imports.
2. **Modern Build Performance**: Fast bundling with Rollup and esbuild.
3. **Clean Environment Configuration**: Easy `import.meta.env.VITE_*` variable integration.

---

## 🛠️ Required Environment Variables

Copy `.env.example` to `.env` or set environment variables before starting:

| Variable | Description | Default Value |
| :--- | :--- | :--- |
| `VITE_API_BASE_URL` | Base URL of `monitor-service` REST API | `http://localhost:8080` |

---

## 🚀 How to Run Locally

### Step 1: Install Dependencies
```bash
cd frontend
npm install
```

### Step 2: Start Development Server
```bash
npm run dev
```

The application will run locally at `http://localhost:3000`.

---

## 💡 Features

- **Live Service Cards**: Displays UP/DOWN status badge with pulsing indicators, real-time uptime %, ping latency (ms), check interval, and last check timestamp.
- **Incident Timeline View**: Chronological log of recent service checks with filters by status (UP/DOWN) and service ID.
- **Add Service Modal**: Interface to add new HTTP endpoints with configurable ping intervals.
- **Service Management**: Quick delete option with confirmation.
- **Auto-Refresh**: Automatically polls the backend every 5 seconds for status updates with visual indicator.
