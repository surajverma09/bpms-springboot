package com.suraj.bpms.project.service;

import com.suraj.bpms.project.entity.AuditLog;
import com.suraj.bpms.project.entity.User;
import com.suraj.bpms.project.exception.AuditLogNotFoundException;
import com.suraj.bpms.project.exception.UserNotFoundException;
import com.suraj.bpms.project.repository.AuditLogRepository;
import com.suraj.bpms.project.repository.UserRepository;
import org.springframework.stereotype.*;

import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditLogService(AuditLogRepository auditLogRepository,
                           UserRepository userRepository){
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    public List<AuditLog> getAllAuditLogs(){
        return auditLogRepository.findAll();
    }

    public AuditLog getAuditLogById(Long id){
        return auditLogRepository.findById(id)
                .orElseThrow(()-> new AuditLogNotFoundException("Audit log not found with this id : " + id));
    }

    public AuditLog createAuditLog(Long userId, AuditLog auditLog){
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(()-> new UserNotFoundException("User not found with this id : "+ userId));

        auditLog.setUser(user);
        
        return auditLogRepository.save(auditLog);
    }
}
