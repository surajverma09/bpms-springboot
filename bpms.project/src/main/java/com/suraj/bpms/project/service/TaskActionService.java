package com.suraj.bpms.project.service;

import com.suraj.bpms.project.entity.Task;
import com.suraj.bpms.project.entity.TaskAction;
import com.suraj.bpms.project.entity.User;
import com.suraj.bpms.project.exception.TaskActionNotFoundException;
import com.suraj.bpms.project.exception.TaskNotFoundException;
import com.suraj.bpms.project.exception.UserNotFoundException;
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


    public List<TaskAction> getAllTaskAction(){
        return taskActionRepository.findAll();
    }


    public TaskAction getTaskActionById(Long id){
        return taskActionRepository.findById(id)
                .orElseThrow(()-> new TaskActionNotFoundException
                        ("Task Action is not found with this id : " + id));
    }

    public TaskAction createTaskAction(Long taskId, Long userId, TaskAction taskAction){

        Task task = taskRepository.findById(taskId)
                .orElseThrow(()-> new TaskNotFoundException
                        ("Task is not found with this id : " + taskId));

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(()-> new UserNotFoundException
                        ("User is not found with this id : " + userId));

        taskAction.setTask(task);
        taskAction.setActionBy(user);

        return taskActionRepository.save(taskAction);
    }

}
