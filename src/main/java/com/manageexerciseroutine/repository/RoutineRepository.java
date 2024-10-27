package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.model.Routine;

import java.sql.SQLException;
import java.util.List;

public interface RoutineRepository {

    // Método para crear una nueva rutina
    void save(Routine routine) throws SQLException;

    // Método para obtener todas las rutinas
    List<Routine> findAll() throws SQLException;

    // Método para actualizar una rutina existente
    void update(Routine routine) throws SQLException;

    // Método para eliminar una rutina por su ID
    void delete(Routine routine) throws SQLException;

    // Método para encontrar una rutina por su ID
    Routine findById(int id) throws SQLException;

    List<Routine> findByTrainerId(int trainerId) throws SQLException;

}
