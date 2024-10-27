package com.manageexerciseroutine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation {
    private int id;
    private int rating;
    private String comment;
    private Date date;
    private Subscriber subscriber;
    private Routine routine;
}
