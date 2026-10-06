package com.suraj.bpms.project.repository;

import com.suraj.bpms.project.entity.ProcessDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessDefinitionRepository extends JpaRepository<ProcessDefinition, Integer> {

}
