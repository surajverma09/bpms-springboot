package com.suraj.bpms.project.dto.taskaction;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TaskActionReqDTO {

    @Size(max = 200)
    private String comment;

}
    