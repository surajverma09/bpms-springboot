package com.suraj.bpms.project.dto.processstep;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProcessStepResponseDTO {

    private Long id;
    private Long processDefinitionId;
    private String processDefinitionName;
    private Integer processDefinitionVersion;
    private String stepName;
    private Integer stepOrder;
    private Long assignedRoleId;
    private String assignedRoleName;
}
