package com.manageexerciseroutine.service;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.ConfiguredExercise;
import com.manageexerciseroutine.model.Routine;
import com.manageexerciseroutine.repository.ConfiguredExerciseRepository;
import com.manageexerciseroutine.repository.ConfiguredExerciseRepositoryImpl;
import com.manageexerciseroutine.repository.RoutineRepository;
import com.manageexerciseroutine.repository.RoutineRepositoryImpl;

import java.util.List;
import java.util.logging.Logger;

public class RoutineService {

    Logger logger = Logger.getLogger(RoutineService.class.getName());

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
        configuredExerciseRepository.deleteByRoutineId(routine.getId());
        routineRepository.delete(routine);
    }

    public List<Routine> findRoutinesByTrainerId(int trainerId) throws DatabaseOperationException {
        return routineRepository.findByTrainerId(trainerId);
    }

    /**
     * Guarda una rutina junto con sus ejercicios configurados.
     *
     * @param routine   La rutina a guardar.
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
     * @param routine   La rutina a actualizar.
     * @param exercises Lista de ejercicios configurados para la rutina.
     * @throws DatabaseOperationException Si ocurre un error durante la actualización.
     */
    public void updateRoutineWithExercises(Routine routine, List<ConfiguredExercise> exercises) throws DatabaseOperationException {
        try {
            // Actualizar los detalles de la rutina
            routineRepository.update(routine);

            // Obtener los ejercicios configurados existentes en la base de datos
            List<ConfiguredExercise> existingExercises = configuredExerciseRepository.findAllByRoutineId(routine.getId());

            // Identificar ejercicios para eliminar
            for (ConfiguredExercise existing : existingExercises) {
                if (!exercises.contains(existing)) {
                    configuredExerciseRepository.delete(existing);
                }
            }

            // Guardar o actualizar los ejercicios configurados
            for (ConfiguredExercise exercise : exercises) {
                if (existingExercises.contains(exercise)) {
                    // Actualizar si ya existe
                    configuredExerciseRepository.update(exercise);
                } else {
                    // Insertar si es nuevo
                    configuredExerciseRepository.save(exercise);
                }
            }
        } catch (Exception e) {
            logger.info("updateRoutineWithExercises. Error: " + e.getMessage());
            throw new DatabaseOperationException("Error al actualizar la rutina con ejercicios configurados", e);
        }
    }

}
