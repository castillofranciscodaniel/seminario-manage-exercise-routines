package com.manageexerciseroutine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    protected int id;
    protected String name;
    protected String email;
    protected String password;

    public User(int trainerId, String trainerName, String trainerEmail) {
        this.id = trainerId;
        this.name = trainerName;
        this.email = trainerEmail;
    }

    public User(int trainerId) {
        this.id = trainerId;
    }
}
