package com.manageexerciseroutine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Routine {
    private int id;
    private String name;
    private String description;
    private int duration;
    private DifficultyLevel difficultyLevel;
    private TrainingType trainingType;
    private Trainer trainer;

    private List<ConfiguredExercise> configuredExercises = new ArrayList<>();

    public Routine(int id, String name, String description, int duration, DifficultyLevel difficultyLevel, TrainingType trainingType,
                   Trainer trainer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.difficultyLevel = difficultyLevel;
        this.trainingType = trainingType;
        this.trainer = trainer;
    }


    public enum DifficultyLevel {
        BEGINNER, INTERMEDIATE, ADVANCED;
    }

    public enum TrainingType {
        STRENGTH, CARDIO, YOGA, FULL_BODY;
    }
}
