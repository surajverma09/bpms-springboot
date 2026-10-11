package com.suraj.bpms.project.controller;

import com.suraj.bpms.project.dto.processinstance.ProcessInstanceCreateDTO;
import com.suraj.bpms.project.dto.processinstance.ProcessInstanceResponseDTO;
import com.suraj.bpms.project.entity.ProcessInstance;
import com.suraj.bpms.project.response.ApiResponse;
import com.suraj.bpms.project.service.ProcessInstanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/process-instance")

public class ProcessInstanceController {

    private final ProcessInstanceService processInstanceService;


    @GetMapping
    public List<ProcessInstanceResponseDTO> getAllProcessInstances(){
        return processInstanceService.getAllProcessInstances();
    }

    @GetMapping("/{id}")
    public ProcessInstanceResponseDTO getProcessInstance(@PathVariable Long id){
         return processInstanceService.getProcessInstanceById(id);
    }

    @PostMapping("/{processDefinitionId}/{userId}")
    public ResponseEntity<ApiResponse> createProcessInstance(@PathVariable Long processDefinitionId,
                                                             @PathVariable Long userId,
                                                             @RequestBody ProcessInstanceCreateDTO processInstanceCreateDTO){
        processInstanceService.createProcessInstance(processDefinitionId, userId, processInstanceCreateDTO);

        return new ResponseEntity<>(new ApiResponse("Process instance is created successfully"), HttpStatus.CREATED);
    }
}
