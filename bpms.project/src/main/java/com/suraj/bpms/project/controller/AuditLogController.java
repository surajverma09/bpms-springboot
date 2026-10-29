package com.suraj.bpms.project.controller;

import com.suraj.bpms.project.dto.auditlog.AuditLogCreateDTO;
import com.suraj.bpms.project.dto.auditlog.AuditLogResponseDTO;
import com.suraj.bpms.project.response.ApiResponse;
import com.suraj.bpms.project.service.AuditLogService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/audit-log")
public class AuditLogController {

    private final AuditLogService auditLogService;

    //Audit log POST endpoint is no longer useful due to auto generation by system
    public ResponseEntity<ApiResponse> createAuditLog(@Valid @RequestBody AuditLogCreateDTO auditLogCreateDTO){
        auditLogService.createAuditLog(auditLogCreateDTO);
        return new ResponseEntity<>(new ApiResponse("Audit log created successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public AuditLogResponseDTO getAuditLogById(@PathVariable Long id){
        return auditLogService.getAuditLogById(id);
    }

    @GetMapping()
    public List<AuditLogResponseDTO> getAllAudit(){
        return auditLogService.getAllAuditLogs();
    }
}
