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
    private String difficultyLevel;
    private String trainingType;
    private Trainer trainer;

    private List<ConfiguredExercise> configuredExercises = new ArrayList<>();

    public Routine(int id, String name, String description, int duration, String difficultyLevel, String trainingType,
                   Trainer trainer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.difficultyLevel = difficultyLevel;
        this.trainingType = trainingType;
        this.trainer = trainer;
    }
}
