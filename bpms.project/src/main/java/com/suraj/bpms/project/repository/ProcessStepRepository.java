package com.suraj.bpms.project.repository;

import com.suraj.bpms.project.entity.ProcessStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcessStepRepository extends JpaRepository<ProcessStep, Long> {

    Optional<ProcessStep> findFirstByProcessDefinitionIdOrderByStepOrderAsc(Long processDefinitionId);

    Optional<ProcessStep> findFirstByProcessDefinitionIdAndStepOrderGreaterThanOrderByStepOrderAsc(
            Long processDefinitionId,
            Integer currentStepOrder
    );
}
