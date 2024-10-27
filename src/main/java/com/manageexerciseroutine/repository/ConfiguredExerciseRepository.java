package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.model.ConfiguredExercise;

import java.sql.SQLException;
import java.util.List;

public interface ConfiguredExerciseRepository {
    List<ConfiguredExercise> findByRoutineId(int routineId) throws SQLException;
}
