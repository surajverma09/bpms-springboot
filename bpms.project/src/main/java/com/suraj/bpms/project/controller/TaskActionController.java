package com.suraj.bpms.project.controller;

import com.suraj.bpms.project.dto.taskaction.TaskActionCreateDTO;
import com.suraj.bpms.project.dto.taskaction.TaskActionResponseDTO;
import com.suraj.bpms.project.response.ApiResponse;
import com.suraj.bpms.project.service.TaskActionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/task-action")
public class TaskActionController {

    private final TaskActionService taskActionService;

    @GetMapping
    public List<TaskActionResponseDTO> getAllTaskAction(){
        return taskActionService.getAllTaskAction();
    }

    @GetMapping("/{id}")
    public TaskActionResponseDTO getTaskActionById(@PathVariable Long id){
        return taskActionService.getTaskActionById(id);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse> createTaskAction(@PathVariable Long userId,
                                                        @Valid @RequestBody TaskActionCreateDTO taskActionCreateDTO){
        taskActionService.createTaskAction(userId, taskActionCreateDTO);
        return new ResponseEntity<>(new ApiResponse("Task action is created successfully"), HttpStatus.CREATED);
    }
}
