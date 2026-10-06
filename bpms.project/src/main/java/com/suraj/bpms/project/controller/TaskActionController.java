package com.suraj.bpms.project.controller;

import com.suraj.bpms.project.entity.TaskAction;
import com.suraj.bpms.project.service.TaskActionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/task-action")
public class TaskActionController {

    private final TaskActionService taskActionService;

    public TaskActionController(TaskActionService taskActionService){
        this.taskActionService = taskActionService;
    }

    @GetMapping
    public List<TaskAction> getAllTaskAction(){
        return taskActionService.getAllTaskAction();
    }

    @GetMapping("/{id}")
    public TaskAction getTaskActionById(@PathVariable Long id){
        return taskActionService.getTaskActionById(id);
    }
}
