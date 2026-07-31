package com.statuspulse.monitor.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateMonitoredServiceRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "URL is required")
    private String url;

    @NotNull(message = "Interval seconds is required")
    @Min(value = 1, message = "Interval must be at least 1 second")
    private Integer intervalSeconds;

    public CreateMonitoredServiceRequest() {
    }

    public CreateMonitoredServiceRequest(String name, String url, Integer intervalSeconds) {
        this.name = name;
        this.url = url;
        this.intervalSeconds = intervalSeconds;
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
}
