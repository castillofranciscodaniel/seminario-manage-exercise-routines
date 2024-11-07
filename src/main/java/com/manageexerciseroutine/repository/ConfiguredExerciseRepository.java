package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.ConfiguredExercise;

import java.sql.SQLException;
import java.util.List;

public interface ConfiguredExerciseRepository {

    // Método para encontrar todos los ejercicios configurados por rutinaId
    List<ConfiguredExercise> findAllByRoutineId(int routineId) throws DatabaseOperationException;

    // Guardar un nuevo ejercicio configurado
    void save(ConfiguredExercise configuredExercise) throws DatabaseOperationException;

    // Actualizar ejercicio configurado
    void update(ConfiguredExercise configuredExercise) throws DatabaseOperationException;

    // Eliminar ejercicio configurado
    void delete(ConfiguredExercise configuredExercise) throws DatabaseOperationException;
}
