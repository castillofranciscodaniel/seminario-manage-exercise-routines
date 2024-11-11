package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.Routine;

import java.util.List;

public interface RoutineRepository {

    // Método para crear una nueva rutina
    void save(Routine routine) throws DatabaseOperationException;

    // Método para obtener todas las rutinas
    List<Routine> findAll(int userId) throws DatabaseOperationException;

    // Método para actualizar una rutina existente
    void update(Routine routine) throws DatabaseOperationException;

    // Método para eliminar una rutina por su ID
    void delete(Routine routine) throws DatabaseOperationException;

    // Método para encontrar una rutina por su ID
    Routine findById(int id) throws DatabaseOperationException;

    List<Routine> findByTrainerId(int trainerId) throws DatabaseOperationException;

}
