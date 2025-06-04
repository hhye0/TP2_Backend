package com.teamproject.TP_backend.exception;

public class PastDateScheduleException extends RuntimeException {
    public PastDateScheduleException(String message) {
        super(message);
    }
}
