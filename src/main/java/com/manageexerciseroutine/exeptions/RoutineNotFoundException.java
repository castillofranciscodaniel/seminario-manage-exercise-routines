package com.manageexerciseroutine.exeptions;

public class RoutineNotFoundException extends RuntimeException {
    public RoutineNotFoundException(int id) {
        super("Routine with ID " + id + " not found.");
    }

    public RoutineNotFoundException(String message) {
        super(message);
    }
}
