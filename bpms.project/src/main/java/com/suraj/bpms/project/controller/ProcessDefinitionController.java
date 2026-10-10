package com.suraj.bpms.project.controller;

import com.suraj.bpms.project.dto.ApiResponse;
import com.suraj.bpms.project.dto.processdefinition.ProcessDefinitionCreateDTO;
import com.suraj.bpms.project.dto.processdefinition.ProcessDefinitionResponseDTO;
import com.suraj.bpms.project.service.ProcessDefinitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/process-definition")
public class ProcessDefinitionController {

    private final ProcessDefinitionService processDefinitionService;

    @GetMapping
    public List<ProcessDefinitionResponseDTO> getAllProcessDefinition() {
        return processDefinitionService.getAllProcessDefinitions();
    }

    @PostMapping("/{createdId}")
    public ResponseEntity<ApiResponse> createProcessDefinition(@Valid @RequestBody ProcessDefinitionCreateDTO processDefinitionCreateDTO,
                                                               @PathVariable Long createdId){
        processDefinitionService.createProcessDefinition(createdId, processDefinitionCreateDTO);

        return new ResponseEntity<>(new ApiResponse("Process Definition is created successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ProcessDefinitionResponseDTO getProcessDefinitionById(@PathVariable Long id){
        return processDefinitionService.getProcessDefinitionById(id);
    }
}