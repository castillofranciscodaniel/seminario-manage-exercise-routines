package com.manageexerciseroutine.service;

import com.manageexerciseroutine.model.Routine;
import com.manageexerciseroutine.repository.RoutineRepository;
import com.manageexerciseroutine.repository.RoutineRepositoryImpl;

import java.sql.SQLException;
import java.util.List;

public class RoutineService {

    private final RoutineRepository routineRepository;

    public RoutineService(RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;
    }

    public List<Routine> findAllRoutines() throws SQLException {
        return routineRepository.findAll();
    }

    public void saveRoutine(Routine routine) throws SQLException {
        routineRepository.save(routine);
    }

    public void updateRoutine(Routine routine) throws SQLException {
        routineRepository.update(routine);
    }

    public void deleteRoutine(Routine routine) throws SQLException {
        routineRepository.delete(routine);
    }

    public List<Routine> findRoutinesByTrainerId(int trainerId) throws SQLException {
        return routineRepository.findByTrainerId(trainerId);
    }
}
