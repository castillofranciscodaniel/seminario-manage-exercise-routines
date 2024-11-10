package com.manageexerciseroutine.service;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.Routine;
import com.manageexerciseroutine.repository.RoutineRepository;
import com.manageexerciseroutine.repository.RoutineRepositoryImpl;

import java.sql.SQLException;
import java.util.List;

public class RoutineService {

    private final RoutineRepository routineRepository;

    public RoutineService() {
        this.routineRepository = new RoutineRepositoryImpl();
    }
    public RoutineService(RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;
    }

    public List<Routine> findAllRoutines() throws DatabaseOperationException {
        return routineRepository.findAll();
    }

    public void saveRoutine(Routine routine) throws DatabaseOperationException {
        routineRepository.save(routine);
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
}
