package com.statuspulse.monitor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "monitored_services")
public class MonitoredService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Service name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "URL is required")
    @Column(nullable = false)
    private String url;

    @NotNull(message = "Interval is required")
    @Min(value = 1, message = "Interval must be at least 1 second")
    @Column(nullable = false)
    private Integer intervalSeconds;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "last_status")
    private ServiceStatus lastStatus;

    @Column(name = "last_checked_at")
    private LocalDateTime lastCheckedAt;

    public MonitoredService() {
    }

    public MonitoredService(Long id, String name, String url, Integer intervalSeconds, LocalDateTime createdAt, ServiceStatus lastStatus, LocalDateTime lastCheckedAt) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.intervalSeconds = intervalSeconds;
        this.createdAt = createdAt;
        this.lastStatus = lastStatus;
        this.lastCheckedAt = lastCheckedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
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
