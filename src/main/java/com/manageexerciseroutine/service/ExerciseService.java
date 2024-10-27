package com.manageexerciseroutine.service;

import com.manageexerciseroutine.model.Exercise;
import com.manageexerciseroutine.repository.ExerciseRepository;

import java.util.List;

public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public void addExercise(String name, String description, int duration, String type) {
        Exercise exercise = new Exercise(0, name, description, duration, type); // ID can be generated dynamically
        exerciseRepository.save(exercise);
    }

    public List<Exercise> getExercises() {
        return exerciseRepository.getAll();
    }

    public void deleteExercise(String name) {
        exerciseRepository.delete(name);
    }

    public Exercise findExercise(String name) {
        return exerciseRepository.findByName(name);
    }
}
