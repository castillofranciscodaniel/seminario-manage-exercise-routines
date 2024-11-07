package com.manageexerciseroutine.exeptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int id) {
        super("User with ID " + id + " not found.");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
