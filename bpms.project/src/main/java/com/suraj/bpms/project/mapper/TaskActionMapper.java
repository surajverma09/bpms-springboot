package com.suraj.bpms.project.mapper;

import com.suraj.bpms.project.dto.taskaction.TaskActionCreateDTO;
import com.suraj.bpms.project.dto.taskaction.TaskActionResponseDTO;
import com.suraj.bpms.project.entity.TaskAction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TaskActionMapper {

    public TaskActionResponseDTO toTaskActionResponseDTO(TaskAction taskAction){
        return new TaskActionResponseDTO(
                taskAction.getId(),
                taskAction.getTask().getId(),
                taskAction.getTask().getStatus(),
                taskAction.getTask().getProcessStep().getStepName(),
                taskAction.getActionBy().getId(),
                taskAction.getActionBy().getName(),
                taskAction.getAction(),
                taskAction.getComment(),
                taskAction.getCreatedAt()
        );
    }
    public TaskAction toTaskActionEntityDTO(TaskActionCreateDTO taskActionCreateDTO){

        TaskAction taskAction = new TaskAction();

        taskAction.setComment(taskActionCreateDTO.getComment());
        taskAction.setAction(taskActionCreateDTO.getAction());

        return taskAction;
    }
}
