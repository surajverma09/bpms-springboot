package com.suraj.bpms.project.dto.processdefinition;


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
public class ProcessDefinitionCreateDTO {


    @NotBlank(message = "Name is required")
    @Size(max = 150, message = "Name cannot exceed more than 150 character")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description should not exceeded than limit")
    private String description;

    @NotNull(message = "Version is required")
    @Positive(message = "Version must be positive or greater than 0")
    private Integer version;
}
