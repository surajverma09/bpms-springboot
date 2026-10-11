package com.suraj.bpms.project.controller;

import com.suraj.bpms.project.dto.processstep.ProcessStepCreateDTO;
import com.suraj.bpms.project.dto.processstep.ProcessStepResponseDTO;
import com.suraj.bpms.project.response.ApiResponse;
import com.suraj.bpms.project.service.ProcessStepService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/process-step")
public class ProcessStepController {

    private final ProcessStepService processStepService;


    @GetMapping
    public List<ProcessStepResponseDTO> getAllProcessStep(){
        return processStepService.getAllProcessStep();
    }

    @GetMapping("/{id}")
    public ProcessStepResponseDTO getProcessStepById(@PathVariable Long id){
        return processStepService.getProcessStepById(id);
    }

    @PostMapping("/{processDefinitionId}/{roleId}")
    public ResponseEntity<ApiResponse> createProcessStep(
                                         @PathVariable Long processDefinitionId,
                                         @PathVariable Long roleId,
                                @Valid @RequestBody ProcessStepCreateDTO processStepCreateDTO){
        processStepService.createProcessStep(processDefinitionId, roleId, processStepCreateDTO);
        return new ResponseEntity<>(new ApiResponse("Process step is created successfully"), HttpStatus.CREATED);
    }
}
