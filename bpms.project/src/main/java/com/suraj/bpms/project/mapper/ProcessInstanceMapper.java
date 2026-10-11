package com.suraj.bpms.project.mapper;


import com.suraj.bpms.project.dto.processinstance.ProcessInstanceResponseDTO;
import com.suraj.bpms.project.entity.ProcessInstance;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import java.util.Map;
import com.suraj.bpms.project.dto.processinstance.ProcessInstanceCreateDTO;
import tools.jackson.core.type.TypeReference;


@AllArgsConstructor
@Component
public class ProcessInstanceMapper {

    private final ObjectMapper objectMapper;

    private Map<String, Object> convertJsonToMap(String json){
        if(json == null || json.isBlank()){
            return null;
        }else{
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        }
    }

    public ProcessInstanceResponseDTO toProcessInstanceResponse(ProcessInstance processInstance){

        return new ProcessInstanceResponseDTO(
                processInstance.getId(),
                processInstance.getProcessDefinition().getId(),
                processInstance.getProcessDefinition().getName(),
                processInstance.getProcessDefinition().getVersion(),
                processInstance.getStartedBy().getId(),
                processInstance.getStartedBy().getName(),
                convertJsonToMap(processInstance.getRequestData()),
                processInstance.getStatus(),
                processInstance.getCreatedAt(),
                processInstance.getUpdatedAt()
        );
    }

    public ProcessInstance toProcessInstanceEntity(ProcessInstanceCreateDTO processInstanceCreateDTO){
        String requestData = processInstanceCreateDTO.getRequestData() == null
                ? null
                : objectMapper.writeValueAsString(processInstanceCreateDTO.getRequestData());

        ProcessInstance processInstance = new ProcessInstance();

        processInstance.setRequestData(requestData);
        return processInstance;
    }

}
