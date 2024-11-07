package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.Subscriber;

import java.util.List;

public interface SubscriberRepository {
    void save(Subscriber subscriber) throws DatabaseOperationException;

    List<Subscriber> findAll() throws DatabaseOperationException;

    Subscriber findById(int id) throws DatabaseOperationException;

    void update(Subscriber subscriber) throws DatabaseOperationException;

    void delete(int id) throws DatabaseOperationException;

    List<Subscriber> findByRegistrationDate(String date) throws DatabaseOperationException;

    Subscriber findByEmailAndPassword(String email, String password) throws DatabaseOperationException;
}
