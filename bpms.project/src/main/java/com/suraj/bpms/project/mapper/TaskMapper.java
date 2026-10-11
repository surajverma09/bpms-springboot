package com.suraj.bpms.project.mapper;

import com.suraj.bpms.project.dto.task.TaskResponseDTO;
import com.suraj.bpms.project.entity.Task;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TaskMapper {

    public TaskResponseDTO toTaskResponseDTO(Task task){
        return new TaskResponseDTO(
                task.getId(),
                task.getProcessInstance().getId(),
                task.getProcessStep().getId(),
                task.getProcessStep().getStepName(),
                task.getAssignedToUser() != null ? task.getAssignedToUser().getId() : null,
                task.getAssignedToUser() != null ? task.getAssignedToUser().getName() : null,
                task.getStatus(),
                task.getCreatedAt(),
                task.getCompletedAt()
        );
    }
}
