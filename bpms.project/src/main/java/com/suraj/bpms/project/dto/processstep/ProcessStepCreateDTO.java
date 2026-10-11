package com.suraj.bpms.project.dto.processstep;

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
public class ProcessStepCreateDTO {

    @Size(max = 100, message = "step name should not exceeded 100 characters")
    @NotBlank(message = "step name should not be blank or null")
    private String stepName;

    @Positive(message = "Step order should be positive or more than 0")
    @NotNull(message = "step order should not be null")
    private Integer stepOrder;
}
