package com.suraj.bpms.project.service;

import com.suraj.bpms.project.dto.taskaction.TaskActionCreateDTO;
import com.suraj.bpms.project.dto.taskaction.TaskActionResponseDTO;
import com.suraj.bpms.project.entity.Task;
import com.suraj.bpms.project.entity.TaskAction;
import com.suraj.bpms.project.entity.User;
import com.suraj.bpms.project.exception.TaskActionNotFoundException;
import com.suraj.bpms.project.exception.TaskNotFoundException;
import com.suraj.bpms.project.exception.UserNotFoundException;
import com.suraj.bpms.project.mapper.TaskActionMapper;
import com.suraj.bpms.project.repository.TaskActionRepository;
import com.suraj.bpms.project.repository.TaskRepository;
import com.suraj.bpms.project.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TaskActionService {

    private final TaskActionRepository taskActionRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskActionMapper taskActionMapper;


    public List<TaskActionResponseDTO> getAllTaskAction(){
        return taskActionRepository.findAll()
                .stream()
                .map(taskActionMapper::toTaskActionResponseDTO)
                .toList();

    }
    public TaskActionResponseDTO getTaskActionById(Long id){
        TaskAction taskAction = taskActionRepository.findById(id)
                .orElseThrow(()-> new TaskActionNotFoundException
                        ("Task Action is not found with this id : " + id));

        return taskActionMapper.toTaskActionResponseDTO(taskAction);
    }

    public TaskActionResponseDTO createTaskAction(Long userId,
                                                  TaskActionCreateDTO taskActionCreateDTO){

        TaskAction taskAction = taskActionMapper.toTaskActionEntityDTO(taskActionCreateDTO);

        Task task = taskRepository.findById(taskActionCreateDTO.getTaskId())
                .orElseThrow(()-> new TaskNotFoundException
                        ("Task is not found with this id : " + taskActionCreateDTO.getTaskId()));

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(()-> new UserNotFoundException
                        ("User is not found with this id : " + userId));

        taskAction.setTask(task);
        taskAction.setActionBy(user);

        TaskAction savedTaskAction = taskActionRepository.save(taskAction);
        return taskActionMapper.toTaskActionResponseDTO(savedTaskAction);
    }

}
