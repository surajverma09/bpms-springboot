package com.suraj.bpms.project.exception;

public class ProcessInstanceNotFoundException extends RuntimeException {
    public ProcessInstanceNotFoundException(String message) {
        super(message);
    }
}
