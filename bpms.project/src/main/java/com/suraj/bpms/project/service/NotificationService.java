package com.suraj.bpms.project.service;

import com.suraj.bpms.project.entity.Notification;
import com.suraj.bpms.project.entity.ProcessInstance;
import com.suraj.bpms.project.entity.Task;
import com.suraj.bpms.project.entity.User;
import com.suraj.bpms.project.exception.NotificationNotFoundException;
import com.suraj.bpms.project.exception.ProcessInstanceNotFoundException;
import com.suraj.bpms.project.exception.TaskNotFoundException;
import com.suraj.bpms.project.exception.UserNotFoundException;
import com.suraj.bpms.project.repository.NotificationRepository;
import com.suraj.bpms.project.repository.ProcessInstanceRepository;
import com.suraj.bpms.project.repository.TaskRepository;
import com.suraj.bpms.project.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ProcessInstanceRepository processInstanceRepository;
    private final TaskRepository taskRepository;


    public List<Notification> getAllNotification(){
        return notificationRepository.findAll();
    }

    public Notification getNotificationById(Long id){
        return notificationRepository.findById(id)
                .orElseThrow(()-> new NotificationNotFoundException
                        ("Notification is not found with this id : "+ id));

    }

    public Notification createNotification(Long userId, Long processInstanceId,
                                           Long taskId, Notification notification){

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(()->new UserNotFoundException("User is not found with this id :" + userId));

        ProcessInstance processInstance = processInstanceRepository.findById(processInstanceId)
                .orElseThrow(()-> new ProcessInstanceNotFoundException
                        ("Process instance is not found with this id : " + processInstanceId));

        Task task =  taskRepository.findById(taskId)
                .orElseThrow(()-> new TaskNotFoundException
                        ("Task is not found with this id : " + taskId));

        notification.setTask(task);
        notification.setProcessInstance(processInstance);
        notification.setUser(user);
        notification.setIsRead(false);

        return notificationRepository.save(notification);
    }


}
