package com.suraj.bpms.project.exception;

public class ProcessStepNotFoundException extends RuntimeException {
    public ProcessStepNotFoundException(String message) {
        super(message);
    }
}
