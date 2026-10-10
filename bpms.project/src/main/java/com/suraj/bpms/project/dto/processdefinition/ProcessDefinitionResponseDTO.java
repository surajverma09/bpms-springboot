package com.suraj.bpms.project.dto.processdefinition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessDefinitionResponseDTO {

    private Long id;
    private String name;
    private String description;
    private Integer version;
    private Long createdById;
    private String createdByName;
}
