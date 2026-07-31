package com.statuspulse.monitor.controller;

import com.statuspulse.monitor.dto.CreateMonitoredServiceRequest;
import com.statuspulse.monitor.dto.MonitoredServiceResponse;
import com.statuspulse.monitor.dto.ServiceCurrentStatusResponse;
import com.statuspulse.monitor.dto.StatusCheckResponse;
import com.statuspulse.monitor.service.MonitoredServiceManagementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class MonitoredServiceController {

    private final MonitoredServiceManagementService serviceManagementService;

    public MonitoredServiceController(MonitoredServiceManagementService serviceManagementService) {
        this.serviceManagementService = serviceManagementService;
    }

    @PostMapping
    public ResponseEntity<MonitoredServiceResponse> createService(@Valid @RequestBody CreateMonitoredServiceRequest request) {
        MonitoredServiceResponse created = serviceManagementService.createService(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MonitoredServiceResponse>> getAllServices() {
        return ResponseEntity.ok(serviceManagementService.getAllServices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonitoredServiceResponse> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceManagementService.getServiceById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MonitoredServiceResponse> updateService(@PathVariable Long id,
                                                                 @Valid @RequestBody CreateMonitoredServiceRequest request) {
        return ResponseEntity.ok(serviceManagementService.updateService(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceManagementService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/current")
    public ResponseEntity<List<ServiceCurrentStatusResponse>> getServiceCurrentStatuses() {
        return ResponseEntity.ok(serviceManagementService.getServiceCurrentStatuses());
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<StatusCheckResponse>> getServiceHistory(
            @PathVariable Long id,
            @RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(serviceManagementService.getServiceHistory(id, limit));
    }

    @GetMapping("/history/recent")
    public ResponseEntity<List<StatusCheckResponse>> getRecentGlobalHistory() {
        return ResponseEntity.ok(serviceManagementService.getRecentGlobalHistory());
    }
}
