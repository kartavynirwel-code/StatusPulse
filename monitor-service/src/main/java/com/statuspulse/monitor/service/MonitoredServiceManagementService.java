package com.statuspulse.monitor.service;

import com.statuspulse.monitor.dto.CreateMonitoredServiceRequest;
import com.statuspulse.monitor.dto.MonitoredServiceResponse;
import com.statuspulse.monitor.dto.ServiceCurrentStatusResponse;
import com.statuspulse.monitor.dto.StatusCheckResponse;
import com.statuspulse.monitor.entity.MonitoredService;
import com.statuspulse.monitor.entity.ServiceStatus;
import com.statuspulse.monitor.entity.StatusCheck;
import com.statuspulse.monitor.repository.MonitoredServiceRepository;
import com.statuspulse.monitor.repository.StatusCheckRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonitoredServiceManagementService {

    private final MonitoredServiceRepository serviceRepository;
    private final StatusCheckRepository checkRepository;

    public MonitoredServiceManagementService(MonitoredServiceRepository serviceRepository,
                                              StatusCheckRepository checkRepository) {
        this.serviceRepository = serviceRepository;
        this.checkRepository = checkRepository;
    }

    public MonitoredServiceResponse createService(CreateMonitoredServiceRequest request) {
        MonitoredService service = new MonitoredService();
        service.setName(request.getName());
        service.setUrl(request.getUrl());
        service.setIntervalSeconds(request.getIntervalSeconds());
        MonitoredService saved = serviceRepository.save(service);
        return new MonitoredServiceResponse(saved);
    }

    public List<MonitoredServiceResponse> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(MonitoredServiceResponse::new)
                .collect(Collectors.toList());
    }

    public MonitoredServiceResponse getServiceById(Long id) {
        MonitoredService service = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Monitored service not found with id: " + id));
        return new MonitoredServiceResponse(service);
    }

    public MonitoredServiceResponse updateService(Long id, CreateMonitoredServiceRequest request) {
        MonitoredService service = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Monitored service not found with id: " + id));
        service.setName(request.getName());
        service.setUrl(request.getUrl());
        service.setIntervalSeconds(request.getIntervalSeconds());
        MonitoredService updated = serviceRepository.save(service);
        return new MonitoredServiceResponse(updated);
    }

    @Transactional
    public void deleteService(Long id) {
        if (!serviceRepository.existsById(id)) {
            throw new IllegalArgumentException("Monitored service not found with id: " + id);
        }
        checkRepository.deleteByServiceId(id);
        serviceRepository.deleteById(id);
    }

    public List<ServiceCurrentStatusResponse> getServiceCurrentStatuses() {
        List<MonitoredService> services = serviceRepository.findAll();
        return services.stream().map(service -> {
            Long totalChecks = checkRepository.countByServiceId(service.getId());
            Long upChecks = checkRepository.countByServiceIdAndStatus(service.getId(), ServiceStatus.UP);
            
            Double uptimePercentage = totalChecks > 0 
                    ? Math.round((upChecks * 100.0 / totalChecks) * 100.0) / 100.0 
                    : 100.0;

            List<StatusCheck> latestChecks = checkRepository.findByServiceIdOrderByCheckedAtDesc(
                    service.getId(), PageRequest.of(0, 1));
            
            Long lastResponseTimeMs = latestChecks.isEmpty() ? null : latestChecks.get(0).getResponseTimeMs();

            return new ServiceCurrentStatusResponse(
                    service.getId(),
                    service.getName(),
                    service.getUrl(),
                    service.getIntervalSeconds(),
                    service.getLastStatus(),
                    service.getLastCheckedAt(),
                    lastResponseTimeMs,
                    uptimePercentage,
                    totalChecks
            );
        }).collect(Collectors.toList());
    }

    public List<StatusCheckResponse> getServiceHistory(Long serviceId, int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit);
        return checkRepository.findByServiceIdOrderByCheckedAtDesc(serviceId, pageRequest).stream()
                .map(StatusCheckResponse::new)
                .collect(Collectors.toList());
    }

    public List<StatusCheckResponse> getRecentGlobalHistory() {
        return checkRepository.findTop100ByOrderByCheckedAtDesc().stream()
                .map(StatusCheckResponse::new)
                .collect(Collectors.toList());
    }
}
