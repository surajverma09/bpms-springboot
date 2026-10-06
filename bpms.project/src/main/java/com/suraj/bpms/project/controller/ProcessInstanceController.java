package com.suraj.bpms.project.controller;

import com.suraj.bpms.project.entity.ProcessInstance;
import com.suraj.bpms.project.service.ProcessInstanceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/process-instance")

public class ProcessInstanceController {

    private final ProcessInstanceService processInstanceService;

    public ProcessInstanceController(ProcessInstanceService processInstanceService){
        this.processInstanceService = processInstanceService;
    }

    @GetMapping
    public List<ProcessInstance> getAllProcessInstances(){
        return processInstanceService.getAllProcessInstances();
    }

    @GetMapping("/{id}")
    public ProcessInstance getProcessInstance(@PathVariable Long id){
         return processInstanceService.getProcessInstanceById(id);
    }

    @PostMapping("/{processDefinitionId}/{userId}")
    public ProcessInstance createProcessInstance(@PathVariable Long processDefinitionId,
                                                 @PathVariable Long userId,
                                                 @RequestBody ProcessInstance processInstance){
        return processInstanceService.createProcessInstance(processDefinitionId, userId, processInstance);
    }
}
