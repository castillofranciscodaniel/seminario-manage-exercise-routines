package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.Trainer;

import java.util.List;

public interface TrainerRepository {
    List<Trainer> findAll() throws DatabaseOperationException;

    Trainer findById(int id) throws DatabaseOperationException;

    void update(Trainer trainer) throws DatabaseOperationException;

    void delete(int id) throws DatabaseOperationException;

    List<Trainer> findBySpecialty(String specialty) throws DatabaseOperationException;

    void save(Trainer trainer) throws DatabaseOperationException;

    Trainer findByEmailAndPassword(String email, String password) throws DatabaseOperationException;
}
