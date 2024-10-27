package com.manageexerciseroutine.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Trainer extends User {
    private String specialty;
    private String biography;

    public Trainer(int id, String name, String email, String password, String specialty, String biography) {
        super(id, name, email, password);
        this.specialty = specialty;
        this.biography = biography;
    }
}
