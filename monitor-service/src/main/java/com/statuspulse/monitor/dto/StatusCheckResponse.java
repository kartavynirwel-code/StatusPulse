package com.statuspulse.monitor.dto;

import com.statuspulse.monitor.entity.ServiceStatus;
import com.statuspulse.monitor.entity.StatusCheck;

import java.time.LocalDateTime;

public class StatusCheckResponse {
    private Long id;
    private Long serviceId;
    private ServiceStatus status;
    private Long responseTimeMs;
    private LocalDateTime checkedAt;

    public StatusCheckResponse() {
    }

    public StatusCheckResponse(StatusCheck check) {
        this.id = check.getId();
        this.serviceId = check.getServiceId();
        this.status = check.getStatus();
        this.responseTimeMs = check.getResponseTimeMs();
        this.checkedAt = check.getCheckedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    public Long getResponseTimeMs() {
        return responseTimeMs;
    }

    public void setResponseTimeMs(Long responseTimeMs) {
        this.responseTimeMs = responseTimeMs;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(LocalDateTime checkedAt) {
        this.checkedAt = checkedAt;
    }
}
