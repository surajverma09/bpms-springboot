package com.suraj.bpms.project.service;

import com.suraj.bpms.project.dto.processinstance.ProcessInstanceCreateDTO;
import com.suraj.bpms.project.dto.processinstance.ProcessInstanceResponseDTO;
import com.suraj.bpms.project.entity.ProcessDefinition;
import com.suraj.bpms.project.entity.ProcessInstance;
import com.suraj.bpms.project.entity.User;
import com.suraj.bpms.project.exception.ProcessDefinitionNotFoundException;
import com.suraj.bpms.project.exception.ProcessInstanceNotFoundException;
import com.suraj.bpms.project.exception.UserNotFoundException;
import com.suraj.bpms.project.mapper.ProcessInstanceMapper;
import com.suraj.bpms.project.repository.ProcessDefinitionRepository;
import com.suraj.bpms.project.repository.ProcessInstanceRepository;
import com.suraj.bpms.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProcessInstanceService {

    private final ProcessInstanceRepository processInstanceRepository;
    private final ProcessDefinitionRepository processDefinitionRepository;
    private final UserRepository userRepository;
    private final ProcessInstanceMapper processInstanceMapper;


    public List<ProcessInstanceResponseDTO> getAllProcessInstances(){
        return processInstanceRepository.findAll()
                .stream()
                .map(processInstanceMapper::toProcessInstanceResponse)
                .toList();
    }



    public ProcessInstanceResponseDTO getProcessInstanceById(Long id){
        ProcessInstance processInstance =  processInstanceRepository.findById(id)
                .orElseThrow(() -> new ProcessInstanceNotFoundException
                        ("Process instance is not found with this id : " + id));

        return processInstanceMapper.toProcessInstanceResponse(processInstance);
    }



    public ProcessInstanceResponseDTO createProcessInstance(Long processDefinitionId,
                                                            Long userId,
                                                            ProcessInstanceCreateDTO processInstanceCreateDTO){
        ProcessDefinition processDefinition = processDefinitionRepository.findById(processDefinitionId)
                .orElseThrow(()-> new ProcessDefinitionNotFoundException
                        ("Process Definition is not found with this id : "+ processDefinitionId));

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new UserNotFoundException("User is not found with this id : " + userId));

        ProcessInstance processInstance = processInstanceMapper
                .toProcessInstanceEntity(processInstanceCreateDTO);

        processInstance.setProcessDefinition(processDefinition);
        processInstance.setStartedBy(user);
        processInstance.setStatus("RUNNING");

        ProcessInstance savedProcessInstance = processInstanceRepository.save(processInstance);
        return processInstanceMapper.toProcessInstanceResponse(savedProcessInstance);
    }
}
