package com.manageexerciseroutine.service;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.Exercise;
import com.manageexerciseroutine.repository.ExerciseRepository;
import com.manageexerciseroutine.repository.ExerciseRepositoryImpl;

import java.util.List;

public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public ExerciseService() {
        this.exerciseRepository = new ExerciseRepositoryImpl();  // Instancia del repositorio
    }

    // Recuperar todos los ejercicios por trainerId
    public List<Exercise> findAllExercisesByTrainerId(int trainerId) throws DatabaseOperationException {
        return exerciseRepository.findAllByTrainerId(trainerId);
    }

    public void saveExercise(Exercise exercise) throws DatabaseOperationException {
        if (exercise.getId() == 0) {
            // Si el ID es 0, creamos un nuevo ejercicio
            exerciseRepository.save(exercise);
        } else {
            // Si el ID es distinto de 0, actualizamos el ejercicio existente
            exerciseRepository.update(exercise);
        }
    }


    // Actualizar un ejercicio existente
    public void updateExercise(Exercise exercise) throws DatabaseOperationException {
        exerciseRepository.update(exercise);
    }

    // Eliminar un ejercicio
    public void deleteExercise(Exercise exercise) throws DatabaseOperationException {
        exerciseRepository.delete(exercise);
    }
}