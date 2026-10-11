package com.suraj.bpms.project.service;

import com.suraj.bpms.project.dto.auditlog.AuditLogCreateDTO;
import com.suraj.bpms.project.dto.auditlog.AuditLogResponseDTO;
import com.suraj.bpms.project.entity.AuditLog;
import com.suraj.bpms.project.entity.User;
import com.suraj.bpms.project.exception.AuditLogNotFoundException;
import com.suraj.bpms.project.exception.UserNotFoundException;
import com.suraj.bpms.project.mapper.AuditLogMapper;
import com.suraj.bpms.project.repository.AuditLogRepository;
import com.suraj.bpms.project.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.*;

import java.util.List;

@AllArgsConstructor
@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;
    private final AuditLogMapper auditLogMapper;

    public List<AuditLogResponseDTO> getAllAuditLogs(){
        return auditLogRepository.findAll()
                .stream().map(auditLogMapper::toAuditLogResponseDTO).toList();
    }

    public AuditLogResponseDTO getAuditLogById(Long id){
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(()-> new AuditLogNotFoundException("Audit log not found with this id : " + id));

        return auditLogMapper.toAuditLogResponseDTO(auditLog);
    }

    public AuditLogResponseDTO createAuditLog(AuditLogCreateDTO auditLogCreateDTO){

        AuditLog auditLog = auditLogMapper.toAuditLogEntity(auditLogCreateDTO);

        User user = userRepository.findByIdAndIsDeletedFalse(auditLogCreateDTO.getUserId())
                .orElseThrow(()-> new UserNotFoundException("User not found with this id : "+ auditLogCreateDTO.getUserId()));

        auditLog.setUser(user);
        
        AuditLog savedAuditlog = auditLogRepository.save(auditLog);

        return auditLogMapper.toAuditLogResponseDTO(savedAuditlog);
    }
}
