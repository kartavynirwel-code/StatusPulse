import logging
from fastapi import FastAPI, BackgroundTasks, status
from fastapi.middleware.cors import CORSMiddleware
from app.schemas import AlertPayload, AlertResponse
from app.services.email_service import send_email_notification
from app.config import settings

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(name)s - %(message)s"
)
logger = logging.getLogger("alert_service")

app = FastAPI(
    title="StatusPulse Alert Service",
    description="Webhook alert listener & email notifier service for StatusPulse",
    version="1.0.0"
)

# CORS Middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/")
def read_root():
    return {"service": "StatusPulse Alert Service", "status": "running"}

@app.post("/alert", response_model=AlertResponse, status_code=status.HTTP_202_ACCEPTED)
async def receive_alert(payload: AlertPayload, background_tasks: BackgroundTasks):
    """
    Webhook endpoint to receive status change notifications from monitor-service.
    Enqueues email sending as a FastAPI background task to ensure fast non-blocking response.
    """
    logger.info("Received alert webhook: Service '%s' is %s at %s", 
                payload.serviceName, payload.status, payload.timestamp)

    # Schedule non-blocking async background email task
    background_tasks.add_task(
        send_email_notification,
        service_name=payload.serviceName,
        status=payload.status,
        timestamp=payload.timestamp
    )

    return AlertResponse(
        message="Alert received and queued for dispatch",
        serviceName=payload.serviceName,
        status=payload.status,
        status_code=202
    )

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("app.main:app", host=settings.ALERT_SERVICE_HOST, port=settings.ALERT_SERVICE_PORT, reload=True)
