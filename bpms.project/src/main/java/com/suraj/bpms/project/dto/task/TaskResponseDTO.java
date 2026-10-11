    package com.suraj.bpms.project.dto.task;

    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.sql.Timestamp;

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public class TaskResponseDTO {

        private Long id;
        private Long processInstanceId;
        private Long processStepId;
        private String processStepName;
        private Long assignedToUserId;
        private String assignedToUserName;
        private String status;
        private Timestamp createdAt;
        private Timestamp completedAt;
    }
