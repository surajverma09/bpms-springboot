package com.suraj.bpms.project.controller;

import com.suraj.bpms.project.entity.*;
import com.suraj.bpms.project.service.ProcessStepService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/process-step")
public class ProcessStepController {

    private final ProcessStepService processStepService;


    @GetMapping
    public List<ProcessStep> getAllProcessStep(){
        return processStepService.getAllProcessStep();
    }

    @GetMapping("/{id}")
    public ProcessStep getProcessStepById(@PathVariable Long id){
        return processStepService.getProcessStepById(id);
    }

    @PostMapping("/{processDefinitionId}/{roleId}")
    public ProcessStep createProcessStep(
                                         @PathVariable Long processDefinitionId,
                                         @PathVariable Long roleId,
                                         @RequestBody ProcessStep processStep){
        return processStepService.createProcessStep(processDefinitionId, roleId, processStep);
    }
}
