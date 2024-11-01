package com.manageexerciseroutine.service;

import com.manageexerciseroutine.model.ConfiguredExercise;
import com.manageexerciseroutine.repository.ConfiguredExerciseRepository;

import java.sql.SQLException;
import java.util.List;

public class ConfiguredExerciseService {

    private final ConfiguredExerciseRepository configuredExerciseRepository;

    public ConfiguredExerciseService(ConfiguredExerciseRepository configuredExerciseRepository) {
        this.configuredExerciseRepository = configuredExerciseRepository;
    }

    public List<ConfiguredExercise> findExercisesByRoutineId(int routineId) throws SQLException {
        return configuredExerciseRepository.findByRoutineId(routineId);
    }
}
