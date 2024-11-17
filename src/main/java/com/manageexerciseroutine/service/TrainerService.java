package com.manageexerciseroutine.service;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.Trainer;
import com.manageexerciseroutine.repository.TrainerRepositoryImpl;

import java.util.List;

public class TrainerService {

    private final TrainerRepositoryImpl trainerRepository;

    public TrainerService() {
        this.trainerRepository = new TrainerRepositoryImpl();
    }


    public TrainerService(TrainerRepositoryImpl trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public void registerTrainer(Trainer trainer) throws DatabaseOperationException {
        trainerRepository.save(trainer);
    }

    public List<Trainer> getAllTrainers() throws DatabaseOperationException {
        return trainerRepository.findAll();
    }

    public Trainer getTrainerById(int id) throws DatabaseOperationException {
        return trainerRepository.findById(id);
    }

    public void updateTrainer(Trainer trainer) throws DatabaseOperationException {
        trainerRepository.update(trainer);
    }

    public void deleteTrainer(int id) throws DatabaseOperationException {
        trainerRepository.delete(id);
    }

    public List<Trainer> getTrainersBySpecialty(String specialty) throws DatabaseOperationException {
        return trainerRepository.findBySpecialty(specialty);
    }

    public Trainer loginTrainer(String email, String password) throws DatabaseOperationException {
        return trainerRepository.findByEmailAndPassword(email, password);
    }
}
