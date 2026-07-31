import smtplib
import logging
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from app.config import settings

logger = logging.getLogger("alert_service")

def send_email_notification(service_name: str, status: str, timestamp: str):
    """
    Sends an email notification via SMTP or logs the alert payload if mock mode is enabled.
    This function runs in a FastAPI background task to ensure fast REST endpoint response times.
    """
    subject = f"[StatusPulse Alert] Service '{service_name}' is now {status}"
    body = (
        f"Alert Notification Details:\n"
        f"---------------------------\n"
        f"Service Name : {service_name}\n"
        f"New Status   : {status}\n"
        f"Timestamp    : {timestamp}\n\n"
        f"Please check StatusPulse Dashboard for details."
    )

    logger.info("Processing alert notification for service '%s' (Status: %s)", service_name, status)

    if settings.SMTP_MOCK_MODE or not settings.SMTP_HOST or settings.SMTP_HOST == "smtp.example.com":
        logger.info(
            "\n================ [MOCK SMTP EMAIL SENT] ================\n"
            "To       : %s\n"
            "From     : %s\n"
            "Subject  : %s\n"
            "Body:\n%s\n"
            "========================================================",
            settings.ALERT_RECIPIENT_EMAIL, settings.SMTP_FROM_EMAIL, subject, body
        )
        return

    # Real SMTP email dispatch
    try:
        msg = MIMEMultipart()
        msg['From'] = settings.SMTP_FROM_EMAIL
        msg['To'] = settings.ALERT_RECIPIENT_EMAIL
        msg['Subject'] = subject
        msg.attach(MIMEText(body, 'plain'))

        server = smtplib.SMTP(settings.SMTP_HOST, settings.SMTP_PORT, timeout=10)
        server.starttls()
        if settings.SMTP_USER and settings.SMTP_PASS:
            server.login(settings.SMTP_USER, settings.SMTP_PASS)
        server.send_message(msg)
        server.quit()

        logger.info("Successfully dispatched email alert to %s", settings.ALERT_RECIPIENT_EMAIL)
    except Exception as e:
        logger.error("Failed to send SMTP email for '%s': %s. Falling back to log print.", service_name, str(e))
        logger.info("[ALERT FALLBACK LOG] Service: %s, Status: %s, Time: %s", service_name, status, timestamp)
