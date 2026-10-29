package com.suraj.bpms.project.service;

import com.suraj.bpms.project.dto.processdefinition.ProcessDefinitionCreateDTO;
import com.suraj.bpms.project.dto.processdefinition.ProcessDefinitionResponseDTO;
import com.suraj.bpms.project.entity.ProcessDefinition;
import com.suraj.bpms.project.entity.User;
import com.suraj.bpms.project.exception.ProcessDefinitionNotFoundException;
import com.suraj.bpms.project.exception.UserNotFoundException;
import com.suraj.bpms.project.mapper.ProcessDefinitionMapper;
import com.suraj.bpms.project.repository.ProcessDefinitionRepository;
import com.suraj.bpms.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProcessDefinitionService {

    private final ProcessDefinitionRepository processDefinitionRepository;
    private final UserRepository userRepository;
    private final ProcessDefinitionMapper processDefinitionMapper;


    public List<ProcessDefinitionResponseDTO> getAllProcessDefinitions() {
        return processDefinitionRepository.findAll()
                .stream()
                .map(processDefinitionMapper::toProcessDefinitionResponseDTO)
                .toList();
    }

    public ProcessDefinitionResponseDTO createProcessDefinition(String authenticatedEmail,
                                           ProcessDefinitionCreateDTO processDefinitionCreateDTO ) {

        User user = userRepository.findByEmailAndIsDeletedFalse(authenticatedEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with email " + authenticatedEmail));

        ProcessDefinition processDefinition = processDefinitionMapper.
                toProcessDefinitionEntity(processDefinitionCreateDTO);

        Optional<ProcessDefinition> latestProcessDefinition =
                processDefinitionRepository.findFirstByNameIgnoreCaseOrderByVersionDesc(processDefinition.getName());

        int nextVersion = 0;
        if(latestProcessDefinition.isPresent()){
            ProcessDefinition latest = latestProcessDefinition.get();
            Integer existingVersion = latest.getVersion();
            nextVersion = existingVersion + 1;
        }else{
            nextVersion = 1;
        }

        processDefinition.setCreatedBy(user);
        processDefinition.setVersion(nextVersion);

        ProcessDefinition savedProcessDefinition =  processDefinitionRepository.save(processDefinition);

        return processDefinitionMapper.toProcessDefinitionResponseDTO(savedProcessDefinition);
    }

    public ProcessDefinitionResponseDTO getProcessDefinitionById(Long id) {
            ProcessDefinition processDefinition = processDefinitionRepository.findById(id)
                .orElseThrow(() -> new ProcessDefinitionNotFoundException("Process Definition Not Found"));

            return processDefinitionMapper.toProcessDefinitionResponseDTO(processDefinition);
    }
}

