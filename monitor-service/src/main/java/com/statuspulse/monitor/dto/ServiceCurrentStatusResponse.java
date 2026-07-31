package com.statuspulse.monitor.dto;

import com.statuspulse.monitor.entity.ServiceStatus;
import java.time.LocalDateTime;

public class ServiceCurrentStatusResponse {
    private Long id;
    private String name;
    private String url;
    private Integer intervalSeconds;
    private ServiceStatus currentStatus;
    private LocalDateTime lastCheckedAt;
    private Long lastResponseTimeMs;
    private Double uptimePercentage;
    private Long totalChecks;

    public ServiceCurrentStatusResponse() {
    }

    public ServiceCurrentStatusResponse(Long id, String name, String url, Integer intervalSeconds, ServiceStatus currentStatus, LocalDateTime lastCheckedAt, Long lastResponseTimeMs, Double uptimePercentage, Long totalChecks) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.intervalSeconds = intervalSeconds;
        this.currentStatus = currentStatus;
        this.lastCheckedAt = lastCheckedAt;
        this.lastResponseTimeMs = lastResponseTimeMs;
        this.uptimePercentage = uptimePercentage;
        this.totalChecks = totalChecks;
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

    public ServiceStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(ServiceStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public LocalDateTime getLastCheckedAt() {
        return lastCheckedAt;
    }

    public void setLastCheckedAt(LocalDateTime lastCheckedAt) {
        this.lastCheckedAt = lastCheckedAt;
    }

    public Long getLastResponseTimeMs() {
        return lastResponseTimeMs;
    }

    public void setLastResponseTimeMs(Long lastResponseTimeMs) {
        this.lastResponseTimeMs = lastResponseTimeMs;
    }

    public Double getUptimePercentage() {
        return uptimePercentage;
    }

    public void setUptimePercentage(Double uptimePercentage) {
        this.uptimePercentage = uptimePercentage;
    }

    public Long getTotalChecks() {
        return totalChecks;
    }

    public void setTotalChecks(Long totalChecks) {
        this.totalChecks = totalChecks;
    }
}
