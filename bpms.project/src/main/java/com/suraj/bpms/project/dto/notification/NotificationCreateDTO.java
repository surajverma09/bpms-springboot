package com.suraj.bpms.project.dto.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationCreateDTO {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be greater than zero")
    private Long userId;

    @Positive(message = "Process instance id must be positive or greater than 0")
    @NotNull(message = "Process instance can't be null")
    private Long processInstanceId;

    @Positive(message = "Must be positive or greater than 0")
    private Long taskId;

    @Size(max = 500, message = "Message should not exceeded more than 500 characters")
    @NotBlank(message = "Message must not be blank")
    private String message;
}
