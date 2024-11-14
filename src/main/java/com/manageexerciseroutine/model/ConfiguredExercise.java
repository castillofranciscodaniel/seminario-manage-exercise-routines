package com.manageexerciseroutine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguredExercise {
    private int orderIndex;
    private int repetitions;
    private int series;
    private Exercise exercise;
    private Routine routine;
    private int rest;

    public ConfiguredExercise(int orderIndex, int repetitions, int series, Exercise exercise, Routine routine) {
        this.orderIndex = orderIndex;
        this.repetitions = repetitions;
        this.series = series;
        this.exercise = exercise;
        this.routine = routine;
    }
}
