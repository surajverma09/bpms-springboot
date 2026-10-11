package com.suraj.bpms.project.service;

import com.suraj.bpms.project.dto.task.TaskResponseDTO;
import com.suraj.bpms.project.entity.*;
import com.suraj.bpms.project.exception.TaskNotFoundException;
import com.suraj.bpms.project.mapper.TaskMapper;
import com.suraj.bpms.project.repository.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.*;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;


    public List<TaskResponseDTO> getAllTask(){
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toTaskResponseDTO)
                .toList();
    }


    public TaskResponseDTO getTaskById(Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task is not found with this id : " + id));

        return taskMapper.toTaskResponseDTO(task);
    }
}
