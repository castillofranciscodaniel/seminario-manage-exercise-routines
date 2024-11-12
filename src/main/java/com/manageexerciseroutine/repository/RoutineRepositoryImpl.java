package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.configuration.DatabaseConnection;
import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.ConfiguredExercise;
import com.manageexerciseroutine.model.Routine;
import com.manageexerciseroutine.model.Subscription;
import com.manageexerciseroutine.model.Trainer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoutineRepositoryImpl implements RoutineRepository {

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
                            resultSet.getString("difficultyLevel"),
                            resultSet.getString("trainingType"),
                            trainer
                    );

                    routines.add(routine);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error executing query to fetch routines", e);
        }

        System.out.println("Routines: " + routines);
        return routines;
    }


    @Override
    public void save(Routine routine) throws DatabaseOperationException {
        String insertRoutineQuery = "INSERT INTO Routines (name, description, duration, difficultyLevel, trainingType, trainer_id) VALUES (?, ?, ?, ?, ?, ?)";
        String insertConfiguredExerciseQuery = "INSERT INTO ConfiguredExercises (orderIndex, repetitions, series, exercise_id, routine_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement routineStmt = connection.prepareStatement(insertRoutineQuery, Statement.RETURN_GENERATED_KEYS)) {
                // Guardar rutina
                routineStmt.setString(1, routine.getName());
                routineStmt.setString(2, routine.getDescription());
                routineStmt.setInt(3, routine.getDuration());
                routineStmt.setString(4, routine.getDifficultyLevel());
                routineStmt.setString(5, routine.getTrainingType());
                routineStmt.setInt(6, routine.getTrainer().getId());
                routineStmt.executeUpdate();

                ResultSet generatedKeys = routineStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int routineId = generatedKeys.getInt(1);
                    routine.setId(routineId);

                    // Guardar ejercicios configurados
                    try (PreparedStatement configStmt = connection.prepareStatement(insertConfiguredExerciseQuery)) {
                        for (ConfiguredExercise configExercise : routine.getConfiguredExercises()) {
                            configExercise.setRoutine(routine);  // Asociar con la rutina actual

                            configStmt.setInt(1, configExercise.getOrderIndex());
                            configStmt.setInt(2, configExercise.getRepetitions());
                            configStmt.setInt(3, configExercise.getSeries());
                            configStmt.setInt(4, configExercise.getExercise().getId());
                            configStmt.setInt(5, routineId);
                            configStmt.addBatch();
                        }
                        configStmt.executeBatch();
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new DatabaseOperationException("Error saving routine and exercises", e);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database connection error", e);
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
