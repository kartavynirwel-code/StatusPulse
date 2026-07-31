package com.statuspulse.monitor.dto;

public class AlertWebhookRequest {
    private String serviceName;
    private String status;
    private String timestamp;

    public AlertWebhookRequest() {
    }

    public AlertWebhookRequest(String serviceName, String status, String timestamp) {
        this.serviceName = serviceName;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
