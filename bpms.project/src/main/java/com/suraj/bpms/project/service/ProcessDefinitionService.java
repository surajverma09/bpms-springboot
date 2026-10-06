package com.suraj.bpms.project.service;

import com.suraj.bpms.project.entity.ProcessDefinition;
import com.suraj.bpms.project.repository.ProcessDefinitionRepository;
import com.suraj.bpms.project.repository.RoleRepository;
import com.suraj.bpms.project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessDefinitionService {

    private final ProcessDefinitionRepository processDefinitionRepository;
    private final RoleRepository roleRepository;

    public ProcessDefinitionService(ProcessDefinitionRepository processDefinitionRepository,
                                    RoleRepository roleRepository) {
        this.processDefinitionRepository = processDefinitionRepository;
        this.roleRepository = roleRepository;
    }

    public List<ProcessDefinition> getAllProcessDefinitions() {
        return processDefinitionRepository.findAll();
    }
}
