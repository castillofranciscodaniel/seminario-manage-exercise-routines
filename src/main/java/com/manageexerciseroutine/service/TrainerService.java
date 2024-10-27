package com.manageexerciseroutine.service;

import com.manageexerciseroutine.model.Trainer;
import com.manageexerciseroutine.repository.TrainerRepositoryImpl;

import java.sql.SQLException;
import java.util.List;

public class TrainerService {

    private final TrainerRepositoryImpl trainerRepository;

    public TrainerService(TrainerRepositoryImpl trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public void registerTrainer(Trainer trainer) throws SQLException {
        trainerRepository.save(trainer);
    }

    public List<Trainer> getAllTrainers() throws SQLException {
        return trainerRepository.findAll();
    }

    public Trainer getTrainerById(int id) throws SQLException {
        return trainerRepository.findById(id);
    }

    public void updateTrainer(Trainer trainer) throws SQLException {
        trainerRepository.update(trainer);
    }

    public void deleteTrainer(int id) throws SQLException {
        trainerRepository.delete(id);
    }

    public List<Trainer> getTrainersBySpecialty(String specialty) throws SQLException {
        return trainerRepository.findBySpecialty(specialty);
    }

    public Trainer loginTrainer(String email, String password) throws SQLException {
        return trainerRepository.findByEmailAndPassword(email, password);
    }
}
