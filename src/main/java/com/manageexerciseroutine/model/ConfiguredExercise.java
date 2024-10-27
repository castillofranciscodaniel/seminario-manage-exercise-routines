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
}
