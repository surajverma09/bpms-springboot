package com.suraj.bpms.project.service;

import com.suraj.bpms.project.entity.ProcessDefinition;
import com.suraj.bpms.project.entity.ProcessInstance;
import com.suraj.bpms.project.entity.User;
import com.suraj.bpms.project.exception.ProcessDefinitionNotFoundException;
import com.suraj.bpms.project.exception.ProcessInstanceNotFoundException;
import com.suraj.bpms.project.exception.UserNotFoundException;
import com.suraj.bpms.project.repository.ProcessDefinitionRepository;
import com.suraj.bpms.project.repository.ProcessInstanceRepository;
import com.suraj.bpms.project.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProcessInstanceService {

    private final ProcessInstanceRepository processInstanceRepository;
    private final ProcessDefinitionRepository processDefinitionRepository;
    private final UserRepository userRepository;


    public List<ProcessInstance> getAllProcessInstances(){
        return processInstanceRepository.findAll();
    }

    public ProcessInstance getProcessInstanceById(Long id){
        return processInstanceRepository.findById(id)
                .orElseThrow(() -> new ProcessInstanceNotFoundException
                        ("Process instance is not found with this id : " + id));
    }
    public ProcessInstance createProcessInstance(Long processDefinitionId,
                                                 Long userId,
                                                 ProcessInstance processInstance){
        ProcessDefinition processDefinition = processDefinitionRepository.findById(processDefinitionId)
                .orElseThrow(()-> new ProcessDefinitionNotFoundException
                        ("Process Definition is not found with this id : "+ processDefinitionId));

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new UserNotFoundException("User is not found with this id : " + userId));

        processInstance.setProcessDefinition(processDefinition);
        processInstance.setStartedBy(user);
        processInstance.setStatus("RUNNING");

        return processInstanceRepository.save(processInstance);
    }
}
