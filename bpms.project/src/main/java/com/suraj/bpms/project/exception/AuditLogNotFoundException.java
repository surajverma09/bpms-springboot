package com.suraj.bpms.project.exception;

public class AuditLogNotFoundException extends RuntimeException{
    public AuditLogNotFoundException(String message){
        super(message);
    }
}
