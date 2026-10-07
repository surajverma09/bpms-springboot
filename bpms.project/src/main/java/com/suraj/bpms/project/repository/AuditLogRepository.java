package com.suraj.bpms.project.repository;

import com.suraj.bpms.project.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
