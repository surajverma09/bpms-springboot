package com.suraj.bpms.project.exception;

public class UnauthorizedUserFoundException extends RuntimeException {
    public UnauthorizedUserFoundException(String message) {
        super(message);
    }
}
