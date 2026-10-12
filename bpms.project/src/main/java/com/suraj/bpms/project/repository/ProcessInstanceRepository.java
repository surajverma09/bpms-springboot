package com.suraj.bpms.project.repository;

import com.suraj.bpms.project.entity.ProcessInstance;
import com.suraj.bpms.project.entity.ProcessStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcessInstanceRepository extends JpaRepository<ProcessInstance, Long> {
}
    