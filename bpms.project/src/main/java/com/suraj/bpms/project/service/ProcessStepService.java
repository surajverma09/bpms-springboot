package com.suraj.bpms.project.service;

import com.suraj.bpms.project.entity.*;
import com.suraj.bpms.project.exception.*;
import com.suraj.bpms.project.repository.*;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProcessStepService {

    private final ProcessStepRepository processStepRepository;
    private final RoleRepository roleRepository;
    private final ProcessDefinitionRepository processDefinitionRepository;


    public List<ProcessStep> getAllProcessStep(){
        return processStepRepository.findAll();
    }

    public ProcessStep getProcessStepById(Long id){
        return processStepRepository.findById(id)
                .orElseThrow(()-> new ProcessStepNotFoundException("Process step not found with this id : " + id));
    }

    public ProcessStep createProcessStep(Long processDefinitionId,
                                         Long roleId,
                                         ProcessStep processStep){

        ProcessDefinition processDefinition = processDefinitionRepository.findById(processDefinitionId)
                .orElseThrow(()-> new ProcessDefinitionNotFoundException
                        ("Process definition is not found with this id : " + processDefinitionId));

        Role role = roleRepository.findById(roleId).
                orElseThrow(()->
                        new RoleNotFoundException("Role not found with this id : " + roleId));

        processStep.setProcessDefinition(processDefinition);
        processStep.setAssignedRole(role);

        return processStepRepository.save(processStep);
    }
}
