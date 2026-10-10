package com.suraj.bpms.project.controller;

import com.suraj.bpms.project.entity.TaskAction;
import com.suraj.bpms.project.service.TaskActionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/task-action")
public class TaskActionController {

    private final TaskActionService taskActionService;

    @GetMapping
    public List<TaskAction> getAllTaskAction(){
        return taskActionService.getAllTaskAction();
    }

    @GetMapping("/{id}")
    public TaskAction getTaskActionById(@PathVariable Long id){
        return taskActionService.getTaskActionById(id);
    }

    @PostMapping("/{taskId}/{userId}")
    public TaskAction createTaskAction(@PathVariable Long taskId,
                                       @PathVariable Long userId,
                                       @RequestBody TaskAction taskAction){
        return taskActionService.createTaskAction(taskId, userId, taskAction);
    }
}
