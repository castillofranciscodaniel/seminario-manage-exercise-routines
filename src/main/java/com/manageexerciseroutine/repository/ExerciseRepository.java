package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.Exercise;

import java.util.List;

public interface ExerciseRepository {
    // Método para encontrar todos los ejercicios por trainerId
    List<Exercise> findAllByTrainerId(int trainerId) throws DatabaseOperationException;

    // Guardar nuevo ejercicio
    void save(Exercise exercise) throws DatabaseOperationException;

    // Actualizar ejercicio existente
    void update(Exercise exercise) throws DatabaseOperationException;

    // Eliminar ejercicio
    void delete(Exercise exercise) throws DatabaseOperationException;
}