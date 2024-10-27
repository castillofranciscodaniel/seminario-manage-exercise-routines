package com.manageexerciseroutine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private int trainerId;
}
