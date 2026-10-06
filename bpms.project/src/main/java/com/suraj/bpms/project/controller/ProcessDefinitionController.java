package com.suraj.bpms.project.controller;

import com.suraj.bpms.project.entity.ProcessDefinition;
import com.suraj.bpms.project.service.ProcessDefinitionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/process-definition")
public class ProcessDefinitionController {

    private ProcessDefinitionService processDefinitionService;

    public ProcessDefinitionController(ProcessDefinitionService processDefinitionService) {
        this.processDefinitionService = processDefinitionService;
    }

    @GetMapping
    public List<ProcessDefinition> getAllProcessDefinition() {
        return processDefinitionService.getAllProcessDefinitions();
    }

    @PostMapping("/{createdId}")
    public ProcessDefinition createProcessDefinition(@RequestBody ProcessDefinition processDefinition,
                                    @PathVariable Long createdId){
        return processDefinitionService.createProcessDefinition(createdId, processDefinition);
    }

    @GetMapping("/{id}")
    public ProcessDefinition getProcessDefinitionById(@PathVariable Long id){
        return processDefinitionService.getProcessDefinitionById(id);
    }
}