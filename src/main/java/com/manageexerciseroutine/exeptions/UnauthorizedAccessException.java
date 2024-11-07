package com.manageexerciseroutine.exeptions;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super("Unauthorized access: " + message);
    }
}
