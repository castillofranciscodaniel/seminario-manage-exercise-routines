package com.manageexerciseroutine.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Trainer extends User {
    private String specialty;
    private String biography;
    private List<Routine> Routine;

    public Trainer(int id, String name, String email, String password, String specialty, String biography) {
        super(id, name, email, password);
        this.specialty = specialty;
        this.biography = biography;
    }

    public Trainer(int trainerId, String trainerName, String trainerEmail, String specialty, String biography) {
        super(trainerId, trainerName, trainerEmail);
        this.specialty = specialty;
        this.biography = biography;
    }

    public Trainer(int trainerId) {
        super(trainerId);
    }
}
