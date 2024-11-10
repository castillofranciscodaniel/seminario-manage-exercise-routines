package com.manageexerciseroutine.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Subscriber extends User {

    public Subscriber(String name, String email, String password, String biography) {
        super(name, email, password, biography);
    }

    public Subscriber(int id, String name, String email, String password) {
        super(id, name, email, password);
    }

    public Subscriber(int id) {
        super(id);
    }

    public Subscriber(int id, String name, String email, String password, Date registrationDate, String biography) {
        super(id, name, email, password, registrationDate, biography);
    }

    public Subscriber(int id, String name, String email, Date registrationDate, String biography) {
        super(id, name, email, registrationDate, biography);
    }
}

