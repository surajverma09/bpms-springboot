    package com.suraj.bpms.project.dto.processinstance;

    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import java.util.Map;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public class ProcessInstanceCreateDTO {

        private Map<String, Object> requestData;

    }