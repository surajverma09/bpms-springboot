package com.suraj.bpms.project.service;

import com.suraj.bpms.project.entity.*;
import com.suraj.bpms.project.exception.TaskNotFoundException;
import com.suraj.bpms.project.repository.*;
import org.springframework.stereotype.*;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProcessInstanceRepository processInstanceRepository;
    private final ProcessStepRepository processStepRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, ProcessInstanceRepository processInstanceRepository,
                       ProcessStepRepository processStepRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.processInstanceRepository = processInstanceRepository;
        this.processStepRepository = processStepRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getAllTask(){
        return taskRepository.findAll();
    }
    public Task getTaskById(Long id){
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task is not found with this id : " + id));
    }
}
