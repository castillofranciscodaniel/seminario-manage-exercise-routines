package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.model.Subscriber;

import java.sql.SQLException;
import java.util.List;

public interface SubscriberRepository {
    void save(Subscriber subscriber) throws SQLException;

    List<Subscriber> findAll() throws SQLException;

    Subscriber findById(int id) throws SQLException;

    void update(Subscriber subscriber) throws SQLException;

    void delete(int id) throws SQLException;

    List<Subscriber> findByRegistrationDate(String date) throws SQLException;
}
