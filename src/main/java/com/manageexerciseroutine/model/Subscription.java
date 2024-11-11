package com.manageexerciseroutine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    private int id;
    private Date startDate;
    private Date endDate;
    private Status status;
    private Subscriber subscriber;
    private Routine routine;

    public static enum Status {
        ACTIVE, ENDED
    }
}
