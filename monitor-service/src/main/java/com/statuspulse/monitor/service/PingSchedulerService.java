package com.statuspulse.monitor.service;

import com.statuspulse.monitor.entity.MonitoredService;
import com.statuspulse.monitor.entity.ServiceStatus;
import com.statuspulse.monitor.entity.StatusCheck;
import com.statuspulse.monitor.repository.MonitoredServiceRepository;
import com.statuspulse.monitor.repository.StatusCheckRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PingSchedulerService {

    private static final Logger log = LoggerFactory.getLogger(PingSchedulerService.class);

    private final MonitoredServiceRepository serviceRepository;
    private final StatusCheckRepository checkRepository;
    private final AlertServiceClient alertServiceClient;
    private final org.springframework.web.client.RestTemplate restTemplate;

    // Virtual thread pool or cached thread pool for concurrent pinging
    private final ExecutorService pingExecutor = Executors.newCachedThreadPool();

    public PingSchedulerService(MonitoredServiceRepository serviceRepository,
                                StatusCheckRepository checkRepository,
                                AlertServiceClient alertServiceClient,
                                org.springframework.web.client.RestTemplate restTemplate) {
        this.serviceRepository = serviceRepository;
        this.checkRepository = checkRepository;
        this.alertServiceClient = alertServiceClient;
        this.restTemplate = restTemplate;
    }

    /**
     * Scheduled background loop running every 2 seconds.
     * Evaluates registered services and triggers health pings if intervalSeconds has elapsed.
     */
    @Scheduled(fixedRate = 2000)
    public void evaluateAndPingServices() {
        List<MonitoredService> services = serviceRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (MonitoredService service : services) {
            boolean shouldPing = service.getLastCheckedAt() == null ||
                    Duration.between(service.getLastCheckedAt(), now).getSeconds() >= service.getIntervalSeconds();

            if (shouldPing) {
                pingExecutor.submit(() -> performPingCheck(service.getId()));
            }
        }
    }

    @Transactional
    public void performPingCheck(Long serviceId) {
        MonitoredService service = serviceRepository.findById(serviceId).orElse(null);
        if (service == null) {
            return;
        }

        long startTime = System.currentTimeMillis();
        ServiceStatus currentStatus = ServiceStatus.DOWN;
        long responseTimeMs = 0;

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    service.getUrl(), HttpMethod.GET, null, String.class);
            responseTimeMs = System.currentTimeMillis() - startTime;

            if (response.getStatusCode().is2xxSuccessful() || response.getStatusCode().is3xxRedirection()) {
                currentStatus = ServiceStatus.UP;
            }
        } catch (Exception e) {
            responseTimeMs = System.currentTimeMillis() - startTime;
            log.debug("Ping check failed for service '{}' ({}): {}", service.getName(), service.getUrl(), e.getMessage());
        }

        LocalDateTime checkTime = LocalDateTime.now();

        // 1. Save status check history record
        StatusCheck check = new StatusCheck();
        check.setServiceId(service.getId());
        check.setStatus(currentStatus);
        check.setResponseTimeMs(responseTimeMs);
        check.setCheckedAt(checkTime);
        checkRepository.save(check);

        // 2. Check for UP->DOWN or DOWN->UP state transition
        ServiceStatus previousStatus = service.getLastStatus();
        if (previousStatus != null && previousStatus != currentStatus) {
            log.warn("Service status change detected for '{}': {} -> {}", service.getName(), previousStatus, currentStatus);
            alertServiceClient.sendStatusChangeAlert(service.getName(), currentStatus);
        }

        // 3. Update MonitoredService record state
        service.setLastStatus(currentStatus);
        service.setLastCheckedAt(checkTime);
        serviceRepository.save(service);
    }
}
