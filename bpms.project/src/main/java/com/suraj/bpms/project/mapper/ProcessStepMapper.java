package com.suraj.bpms.project.mapper;

import com.suraj.bpms.project.dto.processstep.ProcessStepCreateDTO;
import com.suraj.bpms.project.dto.processstep.ProcessStepResponseDTO;
import com.suraj.bpms.project.entity.ProcessStep;
import org.springframework.stereotype.Component;

@Component
public class ProcessStepMapper {

    public ProcessStepResponseDTO toProcessStepResponseDto(ProcessStep processStep){
        return new ProcessStepResponseDTO(
                processStep.getId(),
                processStep.getProcessDefinition().getId(),
                processStep.getProcessDefinition().getName(),
                processStep.getProcessDefinition().getVersion(),
                processStep.getStepName(),
                processStep.getStepOrder(),
                processStep.getAssignedRole().getId(),
                processStep.getAssignedRole().getName()
        );
    }

    public ProcessStep toProcessStepEntity(ProcessStepCreateDTO processStepCreateDTO){
        ProcessStep processStep = new ProcessStep();

        processStep.setStepName(processStepCreateDTO.getStepName());
        processStep.setStepOrder(processStepCreateDTO.getStepOrder());

        return processStep;
    }
}
