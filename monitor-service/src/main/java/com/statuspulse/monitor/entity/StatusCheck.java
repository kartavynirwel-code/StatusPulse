package com.statuspulse.monitor.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "status_checks")
public class StatusCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_id", nullable = false)
    private Long serviceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceStatus status;

    @Column(name = "response_time_ms")
    private Long responseTimeMs;

    @Column(name = "checked_at", nullable = false)
    private LocalDateTime checkedAt;

    public StatusCheck() {
    }

    public StatusCheck(Long id, Long serviceId, ServiceStatus status, Long responseTimeMs, LocalDateTime checkedAt) {
        this.id = id;
        this.serviceId = serviceId;
        this.status = status;
        this.responseTimeMs = responseTimeMs;
        this.checkedAt = checkedAt;
    }

    @PrePersist
    protected void onCheck() {
        if (this.checkedAt == null) {
            this.checkedAt = LocalDateTime.now();
        }
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
