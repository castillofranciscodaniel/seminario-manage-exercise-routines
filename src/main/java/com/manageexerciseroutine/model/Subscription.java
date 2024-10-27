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
    private String status;
    private int userId;
    private int routineId;
}
