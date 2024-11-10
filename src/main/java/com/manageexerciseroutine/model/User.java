package com.manageexerciseroutine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    protected int id;
    protected String name;
    protected String email;
    protected String password;
    protected Date registrationDate;
    private String biography;

    public User(String name, String email, String password, String biography) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.biography = biography;
        this.registrationDate = new Date();
    }

    public User(int id, String name, String email, String password, String biography) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.biography = biography;
        this.registrationDate = new Date();
    }

    public User(int id, String name, String email, Date registrationDate, String biography) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.registrationDate = registrationDate;
        this.biography = biography;
    }

    public User(int id) {
        this.id = id;
    }

    public User(int id, String name, String email, String biography) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.biography = biography;
    }
}
