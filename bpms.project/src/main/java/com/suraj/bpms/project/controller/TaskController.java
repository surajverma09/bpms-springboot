package com.suraj.bpms.project.controller;

import com.suraj.bpms.project.dto.task.TaskResponseDTO;
import com.suraj.bpms.project.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
