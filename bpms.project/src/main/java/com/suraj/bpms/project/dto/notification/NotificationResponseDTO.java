package com.suraj.bpms.project.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class NotificationResponseDTO {
    private Long id;
    private Long userId;
    private Long processInstanceId;
    private Long taskId;
    private String message;
    private Boolean isRead;
    private Timestamp createdAt;
}
