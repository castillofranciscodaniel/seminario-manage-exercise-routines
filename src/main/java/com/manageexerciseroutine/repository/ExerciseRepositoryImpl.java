package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.model.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseRepositoryImpl implements ExerciseRepository {
    private List<Exercise> exercises = new ArrayList<>();

    @Override
    public void save(Exercise exercise) {
        exercises.add(exercise);
    }

    @Override
    public List<Exercise> getAll() {
        return new ArrayList<>(exercises);
    }

    @Override
    public void delete(String name) {
        exercises.removeIf(exercise -> exercise.getName().equals(name));
    }

    @Override
    public Exercise findByName(String name) {
        return exercises.stream()
                .filter(exercise -> exercise.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
