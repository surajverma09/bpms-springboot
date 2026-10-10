package com.suraj.bpms.project.service;

import com.suraj.bpms.project.entity.*;
import com.suraj.bpms.project.exception.TaskNotFoundException;
import com.suraj.bpms.project.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.*;

import java.util.List;

@AllArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProcessInstanceRepository processInstanceRepository;
    private final ProcessStepRepository processStepRepository;
    private final UserRepository userRepository;


    public List<Task> getAllTask(){
        return taskRepository.findAll();
    }
    public Task getTaskById(Long id){
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task is not found with this id : " + id));
    }
}
