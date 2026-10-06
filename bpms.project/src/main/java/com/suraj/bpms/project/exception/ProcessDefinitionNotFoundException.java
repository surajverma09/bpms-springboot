package com.suraj.bpms.project.exception;

public class ProcessDefinitionNotFoundException extends RuntimeException {
    public ProcessDefinitionNotFoundException(String message) {
        super(message);
    }
}
