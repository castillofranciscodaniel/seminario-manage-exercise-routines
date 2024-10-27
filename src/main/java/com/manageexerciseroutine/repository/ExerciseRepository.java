package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.model.Exercise;

import java.util.List;

public interface ExerciseRepository {
    void save(Exercise exercise);

    List<Exercise> getAll();

    void delete(String name);

    Exercise findByName(String name);
}
