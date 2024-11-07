package com.manageexerciseroutine.exeptions;

public class ExerciseNotFoundException extends RuntimeException {
    public ExerciseNotFoundException(int id) {
        super("Exercise with ID " + id + " not found.");
    }
    
    public ExerciseNotFoundException(String message) {
        super(message);
    }
}
