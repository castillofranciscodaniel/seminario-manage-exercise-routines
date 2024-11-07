package com.manageexerciseroutine.exeptions;

public class DatabaseOperationException extends RuntimeException {
    public DatabaseOperationException(String message, Throwable cause) {
        super("Database operation failed: " + message, cause);
    }
}
