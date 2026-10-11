package com.suraj.bpms.project.mapper;

import com.suraj.bpms.project.dto.notification.NotificationCreateDTO;
import com.suraj.bpms.project.dto.notification.NotificationResponseDTO;
import com.suraj.bpms.project.entity.Notification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NotificationMapper {
    public NotificationResponseDTO toNotificationResponseDTO(Notification notification){
        return new NotificationResponseDTO(
                notification.getId(),
                notification.getUser().getId(),
                notification.getProcessInstance().getId(),
                notification.getTask()!= null ? notification.getTask().getId() : null,
                notification.getMessage(),
                notification.getIsRead(),
                notification.getCreatedAt()
        );
    }

    public Notification toNotificationEntity(NotificationCreateDTO notificationCreateDTO){

        Notification notification = new Notification();

        notification.setMessage(notificationCreateDTO.getMessage());
        return notification;
    }
}
