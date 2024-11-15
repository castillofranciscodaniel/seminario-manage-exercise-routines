package com.manageexerciseroutine.service;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.ConfiguredExercise;
import com.manageexerciseroutine.model.Routine;
import com.manageexerciseroutine.repository.ConfiguredExerciseRepository;
import com.manageexerciseroutine.repository.ConfiguredExerciseRepositoryImpl;
import com.manageexerciseroutine.repository.RoutineRepository;
import com.manageexerciseroutine.repository.RoutineRepositoryImpl;

import java.sql.SQLException;
import java.util.List;

public class RoutineService {

    private final RoutineRepository routineRepository;

    private ConfiguredExerciseRepository configuredExerciseRepository;


    public RoutineService() {
        this.routineRepository = new RoutineRepositoryImpl();
        this.configuredExerciseRepository = new ConfiguredExerciseRepositoryImpl();
    }

    public RoutineService(RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;
    }

    public List<Routine> findAllRoutines(int userId) throws DatabaseOperationException {
        return routineRepository.findAll(userId);
    }

    public Routine saveRoutine(Routine routine) throws DatabaseOperationException {
        return routineRepository.save(routine);
    }

    public void updateRoutine(Routine routine) throws DatabaseOperationException {
        routineRepository.update(routine);
    }

    public void deleteRoutine(Routine routine) throws DatabaseOperationException {
        routineRepository.delete(routine);
    }

    public List<Routine> findRoutinesByTrainerId(int trainerId) throws DatabaseOperationException {
        return routineRepository.findByTrainerId(trainerId);
    }

    /**
     * Guarda una rutina junto con sus ejercicios configurados.
     *
     * @param routine La rutina a guardar.
     * @param exercises Los ejercicios configurados a asociar con la rutina.
     * @return La rutina guardada con el ID generado.
     * @throws DatabaseOperationException Si ocurre un error al guardar la rutina o los ejercicios configurados.
     */
    public Routine saveRoutineWithExercises(Routine routine, List<ConfiguredExercise> exercises) throws DatabaseOperationException {
        // Guardar la rutina en la base de datos y obtener el ID generado
        Routine savedRoutine = routineRepository.save(routine);

        // Guardar cada ejercicio configurado, asignando la rutina recién guardada
        for (ConfiguredExercise exercise : exercises) {
            exercise.setRoutine(savedRoutine);  // Asignar la rutina guardada
            configuredExerciseRepository.save(exercise);
        }

        return savedRoutine;
    }


    /**
     * Actualiza una rutina y sus ejercicios configurados asociados.
     *
     * @param routine La rutina a actualizar.
     * @param exercises Lista de ejercicios configurados para la rutina.
     * @throws DatabaseOperationException Si ocurre un error durante la actualización.
     */
    public void updateRoutineWithExercises(Routine routine, List<ConfiguredExercise> exercises) throws DatabaseOperationException {
        // Actualizar la rutina en la base de datos
        routineRepository.update(routine);

        // Obtener los ejercicios configurados existentes para la rutina
        List<ConfiguredExercise> existingExercises = configuredExerciseRepository.findAllByRoutineId(routine.getId());

        // Actualizar o agregar los ejercicios configurados
        for (ConfiguredExercise exercise : exercises) {
            if (existingExercises.contains(exercise)) {
                configuredExerciseRepository.update(exercise);  // Actualizar si ya existe
            } else {
                exercise.setRoutine(routine);  // Asignar la rutina para nuevos ejercicios
                configuredExerciseRepository.save(exercise);   // Guardar si es un nuevo ejercicio
            }
        }

        // Eliminar los ejercicios configurados que ya no están en la lista actual
        for (ConfiguredExercise existingExercise : existingExercises) {
            if (!exercises.contains(existingExercise)) {
                configuredExerciseRepository.delete(existingExercise);
            }
        }
    }
}
