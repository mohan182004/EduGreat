package com.courseplatform.exception;

public class ForbiddenException extends RuntimeException {
    private final String error;

    public ForbiddenException(String error, String message) {
        super(message);
        this.error = error;
    }

    public String getError() { return error; }
}
