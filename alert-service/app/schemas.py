from pydantic import BaseModel, Field
from typing import Optional

class AlertPayload(BaseModel):
    serviceName: str = Field(..., description="Name of the service triggering the alert")
    status: str = Field(..., description="New status of the service (UP or DOWN)")
    timestamp: str = Field(..., description="ISO 8601 timestamp of the incident")

class AlertResponse(BaseModel):
    message: str
    serviceName: str
    status: str
    status_code: int = 200
