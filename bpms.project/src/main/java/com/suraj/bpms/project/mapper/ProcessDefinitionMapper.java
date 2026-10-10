package com.suraj.bpms.project.mapper;

import com.suraj.bpms.project.dto.processdefinition.ProcessDefinitionCreateDTO;
import com.suraj.bpms.project.dto.processdefinition.ProcessDefinitionResponseDTO;
import com.suraj.bpms.project.entity.ProcessDefinition;
import org.springframework.stereotype.Component;

@Component
public class ProcessDefinitionMapper {

    public ProcessDefinitionResponseDTO toProcessDefinitionResponseDTO
            (ProcessDefinition processDefinition){

        return new ProcessDefinitionResponseDTO(
                processDefinition.getId(),
                processDefinition.getName(),
                processDefinition.getDescription(),
                processDefinition.getVersion(),
                processDefinition.getCreatedBy().getId(),
                processDefinition.getCreatedBy().getName()

        );
    }
    public ProcessDefinition toProcessDefinitionEntity(
            ProcessDefinitionCreateDTO processDefinitionCreateDTO
    )
    {
        ProcessDefinition processDefinition = new ProcessDefinition();


        processDefinition.setName(processDefinitionCreateDTO.getName());
        processDefinition.setDescription(processDefinitionCreateDTO.getDescription());
        processDefinition.setVersion(processDefinitionCreateDTO.getVersion());

        return processDefinition;
    }
}
