package com.statuspulse.monitor.service;

import com.statuspulse.monitor.dto.AlertWebhookRequest;
import com.statuspulse.monitor.entity.ServiceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
public class AlertServiceClient {

    private static final Logger log = LoggerFactory.getLogger(AlertServiceClient.class);

    private final RestTemplate restTemplate;

    @Value("${app.alert-service-url}")
    private String alertServiceUrl;

    public AlertServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Triggers an alert POST request to the Python alert-service when a service status changes.
     */
    public void sendStatusChangeAlert(String serviceName, ServiceStatus newStatus) {
        if (alertServiceUrl == null || alertServiceUrl.isBlank()) {
            log.warn("Alert service URL is not configured. Skipping alert notification.");
            return;
        }

        try {
            AlertWebhookRequest payload = new AlertWebhookRequest(
                    serviceName,
                    newStatus.name(),
                    Instant.now().toString()
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<AlertWebhookRequest> request = new HttpEntity<>(payload, headers);

            log.info("Sending alert to {}: Service '{}' transitioned to {}", alertServiceUrl, serviceName, newStatus);
            restTemplate.postForEntity(alertServiceUrl, request, String.class);
            log.info("Successfully sent alert for service '{}'", serviceName);
        } catch (Exception e) {
            log.error("Failed to send alert to alert-service at {}: {}", alertServiceUrl, e.getMessage());
        }
    }
}
