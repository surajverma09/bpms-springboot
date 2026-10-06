package com.suraj.bpms.project.service;

import com.suraj.bpms.project.entity.TaskAction;
import com.suraj.bpms.project.exception.TaskActionNotFoundException;
import com.suraj.bpms.project.repository.TaskActionRepository;
import com.suraj.bpms.project.repository.TaskRepository;
import com.suraj.bpms.project.repository.UserRepository;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskActionService {

    private final TaskActionRepository taskActionRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskActionService(TaskActionRepository taskActionRepository,
                             TaskRepository taskRepository,
                             UserRepository userRepository) {

                            this.taskActionRepository = taskActionRepository;
                            this.taskRepository = taskRepository;
                            this.userRepository = userRepository;
    }

    public List<TaskAction> getAllTaskAction(){
        return taskActionRepository.findAll();
    }
    public TaskAction getTaskActionById(Long id){
        return taskActionRepository.findById(id)
                .orElseThrow(()-> new TaskActionNotFoundException
                        ("Task Action is not found with this id : " + id));
    }
}
