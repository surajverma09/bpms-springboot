package com.suraj.bpms.project.controller;

import com.suraj.bpms.project.entity.AuditLog;
import com.suraj.bpms.project.service.AuditLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-log")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @PostMapping("/{userId}")
    public AuditLog createAuditLog(@PathVariable Long userId,
                                   @RequestBody AuditLog auditLog){
        return auditLogService.createAuditLog(userId, auditLog);
    }
    @GetMapping("/{id}")
    public AuditLog getAuditLogById(@PathVariable Long id){
        return auditLogService.getAuditLogById(id);
    }
    @GetMapping()
    public List<AuditLog> getAllAudit(){
        return auditLogService.getAllAuditLogs();
    }
}
