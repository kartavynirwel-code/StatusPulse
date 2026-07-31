import os
from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    ALERT_SERVICE_HOST: str = os.getenv("ALERT_SERVICE_HOST", "0.0.0.0")
    ALERT_SERVICE_PORT: int = int(os.getenv("ALERT_SERVICE_PORT", "8000"))
    
    SMTP_HOST: str = os.getenv("SMTP_HOST", "smtp.example.com")
    SMTP_PORT: int = int(os.getenv("SMTP_PORT", "587"))
    SMTP_USER: str = os.getenv("SMTP_USER", "")
    SMTP_PASS: str = os.getenv("SMTP_PASS", "")
    SMTP_FROM_EMAIL: str = os.getenv("SMTP_FROM_EMAIL", "alerts@statuspulse.com")
    ALERT_RECIPIENT_EMAIL: str = os.getenv("ALERT_RECIPIENT_EMAIL", "admin@statuspulse.com")
    SMTP_MOCK_MODE: bool = os.getenv("SMTP_MOCK_MODE", "true").lower() in ("true", "1", "yes")

    class Config:
        env_file = ".env"
        extra = "ignore"

settings = Settings()
