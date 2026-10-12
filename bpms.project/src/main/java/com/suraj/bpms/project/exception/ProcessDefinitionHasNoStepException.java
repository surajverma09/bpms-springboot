package com.suraj.bpms.project.exception;

public class ProcessDefinitionHasNoStepException extends RuntimeException {
    public ProcessDefinitionHasNoStepException(String message){
        super(message);
    }
}
