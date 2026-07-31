package com.statuspulse.monitor.controller;

import com.statuspulse.monitor.dto.StatusCheckResponse;
import com.statuspulse.monitor.service.MonitoredServiceManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/status-checks")
public class StatusCheckController {

    private final MonitoredServiceManagementService serviceManagementService;

    public StatusCheckController(MonitoredServiceManagementService serviceManagementService) {
        this.serviceManagementService = serviceManagementService;
    }

    @GetMapping
    public ResponseEntity<List<StatusCheckResponse>> getStatusChecks(
            @RequestParam(required = false) Long serviceId,
            @RequestParam(defaultValue = "50") int limit) {
        if (serviceId != null) {
            return ResponseEntity.ok(serviceManagementService.getServiceHistory(serviceId, limit));
        }
        return ResponseEntity.ok(serviceManagementService.getRecentGlobalHistory());
    }
}
