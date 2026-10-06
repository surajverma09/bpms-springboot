package com.suraj.bpms.project.repository;

import com.suraj.bpms.project.entity.ProcessInstance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessInstanceRepository extends JpaRepository<ProcessInstance, Long> {
}
