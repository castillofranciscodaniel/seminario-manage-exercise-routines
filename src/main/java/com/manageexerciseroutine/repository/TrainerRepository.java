package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.model.Trainer;

import java.sql.SQLException;
import java.util.List;

public interface TrainerRepository {
    List<Trainer> findAll() throws SQLException;

    Trainer findById(int id) throws SQLException;

    void update(Trainer trainer) throws SQLException;

    void delete(int id) throws SQLException;

    List<Trainer> findBySpecialty(String specialty) throws SQLException;

    void save(Trainer trainer) throws SQLException;
}
