package com.suraj.bpms.project.service;

import com.suraj.bpms.project.entity.ProcessDefinition;
import com.suraj.bpms.project.entity.User;
import com.suraj.bpms.project.exception.ProcessDefinitionNotFoundException;
import com.suraj.bpms.project.exception.UserNotFoundException;
import com.suraj.bpms.project.repository.ProcessDefinitionRepository;
import com.suraj.bpms.project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessDefinitionService {

    private final ProcessDefinitionRepository processDefinitionRepository;
    private final UserRepository userRepository;

    public ProcessDefinitionService(ProcessDefinitionRepository processDefinitionRepository,
                                    UserRepository userRepository) {
        this.processDefinitionRepository = processDefinitionRepository;
        this.userRepository = userRepository;
    }

    public List<ProcessDefinition> getAllProcessDefinitions() {
        return processDefinitionRepository.findAll();
    }

    public ProcessDefinition createProcessDefinition(Long createdId,
                                   ProcessDefinition processDefinition ) {
        User user = userRepository.findByIdAndIsDeletedFalse(createdId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + createdId));

        processDefinition.setCreatedBy(user);

        return processDefinitionRepository.save(processDefinition);
    }

    public ProcessDefinition getProcessDefinitionById(Long id) {
        return processDefinitionRepository.findById(id)
                .orElseThrow(() -> new ProcessDefinitionNotFoundException("Process Definition Not Found"));
    }
}

