package com.suraj.bpms.project.dto.taskaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskActionCreateDTO {

    @NotNull(message = "Task ID is required")
    @Positive(message = "Task ID must be greater than zero")
    private Long taskId;

    @NotBlank(message = "Action is required")
    @Size(max = 30, message = "Action must not exceed 30 characters")
    private String action;

    @Size(max = 200, message = "Comment must not exceed 200 characters")
    private String comment;
}
