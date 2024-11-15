package com.manageexerciseroutine.service;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.ConfiguredExercise;
import com.manageexerciseroutine.repository.ConfiguredExerciseRepository;
import com.manageexerciseroutine.repository.ConfiguredExerciseRepositoryImpl;

import java.util.List;


public class ConfiguredExerciseService {

    private final ConfiguredExerciseRepository configuredExerciseRepository;

    public ConfiguredExerciseService() {
        this.configuredExerciseRepository = new ConfiguredExerciseRepositoryImpl();
    }

    public ConfiguredExerciseService(ConfiguredExerciseRepository configuredExerciseRepository) {
        this.configuredExerciseRepository = configuredExerciseRepository;
    }

    public List<ConfiguredExercise> findExercisesByRoutineId(int routineId) throws DatabaseOperationException {
        return configuredExerciseRepository.findAllByRoutineId(routineId);
    }
}
