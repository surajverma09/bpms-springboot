package com.suraj.bpms.project.repository;

import com.suraj.bpms.project.entity.ProcessDefinition;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcessDefinitionRepository extends JpaRepository<ProcessDefinition, Long> {

    Optional<ProcessDefinition> findFirstByNameIgnoreCaseOrderByVersionDesc(String name);
}
