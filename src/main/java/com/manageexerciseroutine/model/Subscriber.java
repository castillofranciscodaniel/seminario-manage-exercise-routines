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

    public Subscriber(int subscriberId, String subscriberName, String subscriberEmail, java.sql.Date registrationDate) {
        super(subscriberId, subscriberName, subscriberEmail);
        this.registrationDate = new Date(registrationDate.getTime());
    }
}
