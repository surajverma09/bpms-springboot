package com.suraj.bpms.project.dto.taskaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskActionResponseDTO {
    private Long id;
    private Long taskId;
    private String taskStatus;
    private String processStepName;
    private Long actionByUserId;
    private String actionByUserName;
    private String action;
    private String comment;
    private Timestamp createdAt;
}
