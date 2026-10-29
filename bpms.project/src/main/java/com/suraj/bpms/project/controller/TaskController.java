package com.suraj.bpms.project.controller;

import com.suraj.bpms.project.dto.task.TaskResponseDTO;
import com.suraj.bpms.project.dto.taskaction.TaskActionReqDTO;
import com.suraj.bpms.project.enums.TaskActionType;
import com.suraj.bpms.project.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<TaskResponseDTO> getAllTask(Authentication authentication){
        return taskService.getAllTask(authentication.getName());
    }


    @GetMapping("/{id}")
    public TaskResponseDTO getTaskById(@PathVariable Long id,  Authentication authentication){
        return taskService.getTaskById(id, authentication.getName());
    }

    @PatchMapping("/{taskId}/action")
    public TaskResponseDTO approveOrReject(
            @Valid @RequestBody TaskActionReqDTO taskActionReqDTO,
            @PathVariable Long taskId,
            Authentication authentication,
            @RequestParam TaskActionType typeAction) {

        return taskService.approveOrReject(taskId, authentication.getName(), typeAction, taskActionReqDTO.getComment());
    }
}
