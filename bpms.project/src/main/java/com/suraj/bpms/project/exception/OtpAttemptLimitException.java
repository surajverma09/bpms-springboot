package com.suraj.bpms.project.exception;

public class OtpAttemptLimitException extends RuntimeException {
    public OtpAttemptLimitException(String message) {
        super(message);
    }
}
