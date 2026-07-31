package com.statuspulse.monitor.repository;

import com.statuspulse.monitor.entity.ServiceStatus;
import com.statuspulse.monitor.entity.StatusCheck;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusCheckRepository extends JpaRepository<StatusCheck, Long> {

    List<StatusCheck> findByServiceIdOrderByCheckedAtDesc(Long serviceId);

    List<StatusCheck> findByServiceIdOrderByCheckedAtDesc(Long serviceId, Pageable pageable);

    List<StatusCheck> findTop100ByOrderByCheckedAtDesc();

    long countByServiceId(Long serviceId);

    long countByServiceIdAndStatus(Long serviceId, ServiceStatus status);

    void deleteByServiceId(Long serviceId);
}
