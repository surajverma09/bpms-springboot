package com.suraj.bpms.project.dto.processinstance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProcessInstanceResponseDTO {

    private Long id;
    private Long processDefinitionId;
    private String processDefinitionName;
    private Integer processDefinitionVersion;
    private Long startedByUserId;
    private String startedByUserName;
    private Map<String, Object> requestData;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}
