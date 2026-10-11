package com.suraj.bpms.project.mapper;

import com.suraj.bpms.project.dto.auditlog.AuditLogCreateDTO;
import com.suraj.bpms.project.dto.auditlog.AuditLogResponseDTO;
import com.suraj.bpms.project.entity.AuditLog;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuditLogMapper {

    public AuditLogResponseDTO toAuditLogResponseDTO(AuditLog auditLog){
        return new AuditLogResponseDTO(
                auditLog.getId(),
                auditLog.getUser().getId(),
                auditLog.getAction(),
                auditLog.getEntityType(),
                auditLog.getEntityId(),
                auditLog.getDetails(),
                auditLog.getCreatedAt()
        );
    }

    public AuditLog toAuditLogEntity(AuditLogCreateDTO auditLogCreateDTO){

        AuditLog auditLog = new AuditLog();

        auditLog.setAction(auditLogCreateDTO.getAction());
        auditLog.setEntityType(auditLogCreateDTO.getEntityType());
        auditLog.setEntityId(auditLogCreateDTO.getEntityId());
        auditLog.setDetails(auditLogCreateDTO.getDetails());

        return auditLog;
    }
}