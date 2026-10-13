package com.suraj.bpms.project.controller;

import com.suraj.bpms.project.dto.task.TaskResponseDTO;
import com.suraj.bpms.project.enums.TaskActionType;
import com.suraj.bpms.project.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<TaskResponseDTO> getAllTask(){
        return taskService.getAllTask();
    }


    @GetMapping("/{id}")
    public TaskResponseDTO getTaskById(@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    @PatchMapping("/{taskId}/action")
    public TaskResponseDTO approveOrReject(
            @PathVariable Long taskId,
            @RequestParam Long userId,
            @RequestParam TaskActionType typeAction) {

        return taskService.approveOrReject(taskId, userId, typeAction);
    }
}
