package com.manageexerciseroutine.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Subscriber extends User {
    private Date registrationDate;

    public Subscriber(int id, String name, String email, String password, Date registrationDate) {
        super(id, name, email, password);
        this.registrationDate = registrationDate;
    }
}
