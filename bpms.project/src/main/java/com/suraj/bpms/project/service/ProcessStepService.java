package com.suraj.bpms.project.service;

import com.suraj.bpms.project.dto.processstep.ProcessStepCreateDTO;
import com.suraj.bpms.project.dto.processstep.ProcessStepResponseDTO;
import com.suraj.bpms.project.entity.*;
import com.suraj.bpms.project.exception.*;
import com.suraj.bpms.project.mapper.ProcessStepMapper;
import com.suraj.bpms.project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProcessStepService {

    private final ProcessStepRepository processStepRepository;
    private final RoleRepository roleRepository;
    private final ProcessDefinitionRepository processDefinitionRepository;
    private final ProcessStepMapper processStepMapper;


    public List<ProcessStepResponseDTO> getAllProcessStep(){
        return processStepRepository.findAll()
                .stream()
                .map(processStepMapper::toProcessStepResponseDto)
                .toList();
    }

    public ProcessStepResponseDTO getProcessStepById(Long id){
        ProcessStep processStep =  processStepRepository.findById(id)
                .orElseThrow(()-> new
                        ProcessStepNotFoundException("Process step not found with this id : " + id));

        return processStepMapper.toProcessStepResponseDto(processStep);
    }

    public ProcessStepResponseDTO createProcessStep(Long processDefinitionId,
                                         Long roleId,
                                         ProcessStepCreateDTO processStepCreateDTO){

        ProcessStep processStep = processStepMapper.toProcessStepEntity(processStepCreateDTO);

        ProcessDefinition processDefinition = processDefinitionRepository.findById(processDefinitionId)
                .orElseThrow(()-> new ProcessDefinitionNotFoundException
                        ("Process definition is not found with this id : " + processDefinitionId));

        Role role = roleRepository.findById(roleId).
                orElseThrow(()->
                        new RoleNotFoundException("Role not found with this id : " + roleId));

        processStep.setProcessDefinition(processDefinition);
        processStep.setAssignedRole(role);



        ProcessStep savedProcessStep =  processStepRepository.save(processStep);
        return processStepMapper.toProcessStepResponseDto(savedProcessStep);
    }
}
