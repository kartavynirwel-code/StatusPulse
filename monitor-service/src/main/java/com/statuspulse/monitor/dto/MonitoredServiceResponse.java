package com.statuspulse.monitor.dto;

import com.statuspulse.monitor.entity.MonitoredService;
import com.statuspulse.monitor.entity.ServiceStatus;

import java.time.LocalDateTime;

public class MonitoredServiceResponse {
    private Long id;
    private String name;
    private String url;
    private Integer intervalSeconds;
    private LocalDateTime createdAt;
    private ServiceStatus lastStatus;
    private LocalDateTime lastCheckedAt;

    public MonitoredServiceResponse() {
    }

    public MonitoredServiceResponse(MonitoredService service) {
        this.id = service.getId();
        this.name = service.getName();
        this.url = service.getUrl();
        this.intervalSeconds = service.getIntervalSeconds();
        this.createdAt = service.getCreatedAt();
        this.lastStatus = service.getLastStatus();
        this.lastCheckedAt = service.getLastCheckedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getIntervalSeconds() {
        return intervalSeconds;
    }

    public void setIntervalSeconds(Integer intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ServiceStatus getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(ServiceStatus lastStatus) {
        this.lastStatus = lastStatus;
    }

    public LocalDateTime getLastCheckedAt() {
        return lastCheckedAt;
    }

    public void setLastCheckedAt(LocalDateTime lastCheckedAt) {
        this.lastCheckedAt = lastCheckedAt;
    }
}
