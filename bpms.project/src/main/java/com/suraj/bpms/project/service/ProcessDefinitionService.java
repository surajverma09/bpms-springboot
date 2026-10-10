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
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public ProcessDefinitionResponseDTO createProcessDefinition(Long createdId,
                                           ProcessDefinitionCreateDTO processDefinitionCreateDTO ) {

        ProcessDefinition processDefinition = processDefinitionMapper.
                toProcessDefinitionEntity(processDefinitionCreateDTO);

        User user = userRepository.findByIdAndIsDeletedFalse(createdId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + createdId));

        processDefinition.setCreatedBy(user);

        ProcessDefinition savedProcessDefinition =  processDefinitionRepository.save(processDefinition);

        return processDefinitionMapper.toProcessDefinitionResponseDTO(savedProcessDefinition);
    }

    public ProcessDefinitionResponseDTO getProcessDefinitionById(Long id) {
            ProcessDefinition processDefinition = processDefinitionRepository.findById(id)
                .orElseThrow(() -> new ProcessDefinitionNotFoundException("Process Definition Not Found"));

            return processDefinitionMapper.toProcessDefinitionResponseDTO(processDefinition);
    }
}

