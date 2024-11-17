package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.configuration.DatabaseConnection;
import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.Routine;
import com.manageexerciseroutine.model.Subscription;
import com.manageexerciseroutine.model.Trainer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RoutineRepositoryImpl implements RoutineRepository {

    Logger logger = Logger.getLogger(RoutineRepositoryImpl.class.getName());

    // RoutineRepositoryImpl.java
    @Override
    public List<Routine> findAll(int userId) throws DatabaseOperationException {
        String query = "SELECT DISTINCT r.id, r.name, r.description, r.duration, r.difficultyLevel, r.trainingType, " +
                "t.id AS trainerId, t.name AS trainerName, t.email AS trainerEmail, t.specialty, t.biography " +
                "FROM Routines r " +
                "JOIN Trainers t ON r.trainer_id = t.id " +
                "LEFT JOIN Subscriptions s ON s.routine_id = r.id AND s.user_id = ? " +
                "WHERE s.id IS NULL OR (s.status = ? AND s.endDate IS NOT NULL) " +
                "AND NOT EXISTS (" +
                "    SELECT 1 FROM Subscriptions s2 " +
                "    WHERE s2.routine_id = r.id AND s2.user_id = ? AND s2.status = ? AND s2.endDate IS NULL" +
                ")";

        List<Routine> routines = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);  // Asignar el ID del usuario para la consulta principal
            statement.setString(2, Subscription.Status.ENDED.toString());  // Filtrar por suscripciones terminadas
            statement.setInt(3, userId);  // Usado en el subquery
            statement.setString(4, Subscription.Status.ACTIVE.toString()); // Verificar si existe una suscripción activa

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Trainer trainer = new Trainer(
                            resultSet.getInt("trainerId"),
                            resultSet.getString("trainerName"),
                            resultSet.getString("trainerEmail"),
                            resultSet.getString("specialty"),
                            resultSet.getString("biography")
                    );

                    Routine routine = new Routine(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getInt("duration"),
                            Routine.DifficultyLevel.valueOf(resultSet.getString("difficultyLevel")),
                            Routine.TrainingType.valueOf(resultSet.getString("trainingType")),
                            trainer
                    );

                    routines.add(routine);
                }
            }
        } catch (SQLException e) {
            logger.info("findAll. Error executing query. error: " + e.getMessage());
            throw new DatabaseOperationException("Error executing query to fetch routines", e);
        }

        System.out.println("Routines: " + routines);
        return routines;
    }


    @Override
    public Routine save(Routine routine) throws DatabaseOperationException {
        String query = "INSERT INTO Routines (name, description, duration, difficultyLevel, trainingType, trainer_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, routine.getName());
            statement.setString(2, routine.getDescription());
            statement.setInt(3, routine.getDuration());
            statement.setString(4, routine.getDifficultyLevel().toString());
            statement.setString(5, routine.getTrainingType().toString());
            statement.setInt(6, routine.getTrainer().getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Error al guardar la rutina, no se pudo insertar.");
            }

            // Obtener el ID generado
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    routine.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Error al guardar la rutina, no se generó un ID.");
                }
            }
            return routine;

        } catch (SQLException e) {
            logger.info("save. Error executing query. error: " + e.getMessage());
            throw new DatabaseOperationException("Error al guardar la rutina", e);
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
            statement.setString(4, routine.getDifficultyLevel().name());
            statement.setString(5, routine.getTrainingType().name());
            statement.setInt(6, routine.getTrainer().getId());  // Ahora es id
            statement.setInt(7, routine.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.info("update. Error executing query. error: " + e.getMessage());
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
            logger.info("delete. Error executing query. error: " + e.getMessage());
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
                            Routine.DifficultyLevel.valueOf(resultSet.getString("difficultyLevel")),
                            Routine.TrainingType.valueOf(resultSet.getString("trainingType")),
                            trainer  // Asignar el objeto Trainer
                    );
                }
            }
        } catch (SQLException e) {
            logger.info("findById. Error executing query. error: " + e.getMessage());
            throw new DatabaseOperationException("Error executing query", e);
        }
        return null;  // Retorna null si no se encuentra la rutina con el id proporcionado
    }

    @Override
    public List<Routine> findByTrainerId(int trainerId) throws DatabaseOperationException {
        String query = "SELECT id, name, description, duration, difficultyLevel, trainingType  FROM Routines WHERE trainer_id = ?";


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
                            Routine.DifficultyLevel.valueOf(resultSet.getString("difficultyLevel")),
                            Routine.TrainingType.valueOf(resultSet.getString("trainingType")),
                            null
                    );
                    routines.add(routine);
                }
                return routines;
            }
        } catch (SQLException e) {
            logger.info("findByTrainerId. Error executing query. error: " + e.getMessage());
            throw new DatabaseOperationException("Error executing query", e);
        }
    }
}
