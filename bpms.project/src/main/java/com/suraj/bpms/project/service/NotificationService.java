package com.suraj.bpms.project.service;

import com.suraj.bpms.project.dto.notification.NotificationCreateDTO;
import com.suraj.bpms.project.dto.notification.NotificationResponseDTO;
import com.suraj.bpms.project.entity.Notification;
import com.suraj.bpms.project.entity.ProcessInstance;
import com.suraj.bpms.project.entity.Task;
import com.suraj.bpms.project.entity.User;
import com.suraj.bpms.project.exception.NotificationNotFoundException;
import com.suraj.bpms.project.exception.ProcessInstanceNotFoundException;
import com.suraj.bpms.project.exception.TaskNotFoundException;
import com.suraj.bpms.project.exception.UserNotFoundException;
import com.suraj.bpms.project.mapper.NotificationMapper;
import com.suraj.bpms.project.repository.NotificationRepository;
import com.suraj.bpms.project.repository.ProcessInstanceRepository;
import com.suraj.bpms.project.repository.TaskRepository;
import com.suraj.bpms.project.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@AllArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ProcessInstanceRepository processInstanceRepository;
    private final TaskRepository taskRepository;
    private final NotificationMapper notificationMapper;


    public List<NotificationResponseDTO> getAllNotification(Long userId){
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(notificationMapper::toNotificationResponseDTO)
                .toList();
    }


    public NotificationResponseDTO getNotificationById(Long id, Long userId){
        Notification notification =  notificationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(()-> new NotificationNotFoundException
                        ("Notification is not found with this id : "+ id));

        return notificationMapper.toNotificationResponseDTO(notification);

    }

    public void updateNotificationRead(Long id, Long userId){

        Notification notification = notificationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(()-> new NotificationNotFoundException
                        ("Notification is not found with this id : "+ id));

        notification.setIsRead(true);

        notificationRepository.save(notification);
    }

    public NotificationResponseDTO createNotification(NotificationCreateDTO notificationCreateDTO){

        Notification notification = notificationMapper.toNotificationEntity(notificationCreateDTO);

        User user = userRepository.findByIdAndIsDeletedFalse(notificationCreateDTO.getUserId())
                .orElseThrow(()->new UserNotFoundException("User is not found with this id :" + notificationCreateDTO.getUserId()));

        ProcessInstance processInstance = processInstanceRepository.findById(notificationCreateDTO.getProcessInstanceId())
                .orElseThrow(()-> new ProcessInstanceNotFoundException
                        ("Process instance is not found with this id : " + notificationCreateDTO.getProcessInstanceId()));

        Task task = null;

        if(notificationCreateDTO.getTaskId() != null) {
            task = taskRepository.findById(notificationCreateDTO.getTaskId())
                    .orElseThrow(() -> new TaskNotFoundException
                            ("Task is not found with this id : " + notificationCreateDTO.getTaskId()));
        }
        notification.setTask(task);
        notification.setProcessInstance(processInstance);
        notification.setUser(user);
        notification.setIsRead(false);

        Notification savedNotification = notificationRepository.save(notification);
        return notificationMapper.toNotificationResponseDTO(savedNotification);
    }

    public List<NotificationResponseDTO> getUnreadNotifications(Long userId){
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
                .stream()
                .map(notificationMapper::toNotificationResponseDTO)
                .toList();
    }

    public Long countUnreadNotification(Long userId){

        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

}
