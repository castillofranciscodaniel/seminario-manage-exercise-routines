package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.Exercise;
import com.manageexerciseroutine.model.Trainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ExerciseRepositoryImpl implements ExerciseRepository {

    Logger logger = Logger.getLogger(ExerciseRepositoryImpl.class.getName());

    // Método para encontrar todos los ejercicios por trainerId
    public List<Exercise> findAllByTrainerId(int trainerId) throws DatabaseOperationException {
        String query = "SELECT e.id, e.name, e.description, e.duration, e.type, " +
                "t.id as trainerId, t.name as trainerName, t.email as trainerEmail, t.specialty, t.biography " +
                "FROM Exercises e " +
                "JOIN Trainers t ON e.trainer_id = t.id " +
                "WHERE t.id = ?";

        List<Exercise> exercises = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, trainerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Construir el objeto Trainer
                    Trainer trainer = new Trainer(
                            resultSet.getInt("trainerId"),
                            resultSet.getString("trainerName"),
                            resultSet.getString("trainerEmail"),
                            resultSet.getString("specialty"),
                            resultSet.getString("biography")
                    );

                    // Construir el objeto Exercise
                    Exercise exercise = new Exercise(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getInt("duration"),
                            resultSet.getString("type"),
                            trainer  // Asociar el objeto Trainer al ejercicio
                    );
                    exercises.add(exercise);
                }
            }
        } catch (SQLException e) {
            logger.info("findAllByTrainerId. Error executing query. error" + e.getMessage());
            throw new DatabaseOperationException("Error executing query", e);
        }
        return exercises;
    }

    // Guardar un nuevo ejercicio
    public void save(Exercise exercise) throws DatabaseOperationException {
        String query = "INSERT INTO Exercises (name, description, duration, type, trainer_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, exercise.getName());
            statement.setString(2, exercise.getDescription());
            statement.setInt(3, exercise.getDuration());
            statement.setString(4, exercise.getType());
            statement.setInt(5, exercise.getTrainer().getId());  // Obtener el id del entrenador

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.info("save. Error executing query. error" + e.getMessage());
            throw new DatabaseOperationException("Error executing query", e);
        }
    }

    // Actualizar ejercicio existente
    public void update(Exercise exercise) throws DatabaseOperationException {
        String query = "UPDATE Exercises SET name = ?, description = ?, duration = ?, type = ?, trainer_id = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, exercise.getName());
            statement.setString(2, exercise.getDescription());
            statement.setInt(3, exercise.getDuration());
            statement.setString(4, exercise.getType());
            statement.setInt(5, exercise.getTrainer().getId());  // Obtener el id del entrenador
            statement.setInt(6, exercise.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.info("update. Error executing query. error" + e.getMessage());
            throw new DatabaseOperationException("Error executing query", e);
        }
    }

    // Eliminar ejercicio
    public void delete(Exercise exercise) throws DatabaseOperationException {
        String query = "DELETE FROM Exercises WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, exercise.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.info("delete. Error executing query. error: " + e.getMessage());
            if (e.getMessage().contains("foreign key constraint fails")) {
                throw new DatabaseOperationException("No se puede eliminar el ejercicio porque está asociado a una rutina", e);
            }
            throw new DatabaseOperationException("Error executing query", e);
        }
    }
}