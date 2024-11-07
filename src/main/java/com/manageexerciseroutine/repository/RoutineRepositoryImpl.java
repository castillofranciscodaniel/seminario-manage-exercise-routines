package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.configuration.DatabaseConnection;
import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.Routine;
import com.manageexerciseroutine.model.Trainer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoutineRepositoryImpl implements RoutineRepository {

    @Override
    public List<Routine> findAll() throws DatabaseOperationException {
        String query = "SELECT r.id, r.name, r.description, r.duration, r.difficultyLevel, r.trainingType, " +
                "t.id as id, t.name as trainerName, t.email as trainerEmail, t.specialty, t.biography " +
                "FROM Routines r " +
                "JOIN Trainers t ON r.trainer_id = t.id";

        List<Routine> routines = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Trainer trainer = new Trainer(
                        resultSet.getInt("id"),  // Ahora es solo id
                        resultSet.getString("trainerName"),
                        resultSet.getString("trainerEmail"),
                        resultSet.getString("specialty"),
                        resultSet.getString("biography")
                );

                Routine routine = new Routine(
                        resultSet.getInt("id"),  // id de la rutina
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("duration"),
                        resultSet.getString("difficultyLevel"),
                        resultSet.getString("trainingType"),
                        trainer  // Aquí referenciamos al objeto Trainer
                );

                routines.add(routine);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error executing query", e);
        }
        return routines;
    }

    @Override
    public void save(Routine routine) throws DatabaseOperationException {
        String query = "INSERT INTO Routines (name, description, duration, difficultyLevel, trainingType, trainer_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, routine.getName());
            statement.setString(2, routine.getDescription());
            statement.setInt(3, routine.getDuration());
            statement.setString(4, routine.getDifficultyLevel());
            statement.setString(5, routine.getTrainingType());
            statement.setInt(6, routine.getTrainer().getId());  // Ahora es id
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error executing query", e);
        }
    }

    @Override
    public void update(Routine routine) throws DatabaseOperationException {
        String query = "UPDATE Routines SET name = ?, description = ?, duration = ?, difficultyLevel = ?, trainingType = ?, trainer_id = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, routine.getName());
            statement.setString(2, routine.getDescription());
            statement.setInt(3, routine.getDuration());
            statement.setString(4, routine.getDifficultyLevel());
            statement.setString(5, routine.getTrainingType());
            statement.setInt(6, routine.getTrainer().getId());  // Ahora es id
            statement.setInt(7, routine.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error executing query", e);
        }
    }

    @Override
    public void delete(Routine routine) throws DatabaseOperationException {
        String query = "DELETE FROM Routines WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, routine.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error executing query", e);
        }
    }

    @Override
    public Routine findById(int id) throws DatabaseOperationException {
        String query = "SELECT r.id, r.name, r.description, r.duration, r.difficultyLevel, r.trainingType, " +
                "t.id as id, t.name as trainerName, t.email as trainerEmail, t.specialty, t.biography " +
                "FROM Routines r " +
                "JOIN Trainers t ON r.trainer_id = t.id " +
                "WHERE r.id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Construir el objeto Trainer
                    Trainer trainer = new Trainer(
                            resultSet.getInt("id"),  // id del entrenador
                            resultSet.getString("trainerName"),
                            resultSet.getString("trainerEmail"),
                            resultSet.getString("specialty"),
                            resultSet.getString("biography")
                    );

                    // Construir el objeto Routine
                    return new Routine(
                            resultSet.getInt("id"),  // id de la rutina
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getInt("duration"),
                            resultSet.getString("difficultyLevel"),
                            resultSet.getString("trainingType"),
                            trainer  // Asignar el objeto Trainer
                    );
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error executing query", e);
        }
        return null;  // Retorna null si no se encuentra la rutina con el id proporcionado
    }

    @Override
    public List<Routine> findByTrainerId(int trainerId) throws DatabaseOperationException {
        String query = "SELECT id, name, description, duration FROM Routines WHERE trainer_id = ?";


        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, trainerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Routine> routines = new ArrayList<>();
                while (resultSet.next()) {
                    Routine routine = new Routine(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getInt("duration"),
                            null, null, null
                    );
                    routines.add(routine);
                }
                return routines;
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error executing query", e);
        }
    }
}
