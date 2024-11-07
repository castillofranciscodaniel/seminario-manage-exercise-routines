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

    // Guardar un nuevo ejercicio
    public void saveExercise(Exercise exercise) throws DatabaseOperationException {
        exerciseRepository.save(exercise);
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