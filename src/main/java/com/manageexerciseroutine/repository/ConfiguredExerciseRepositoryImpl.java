package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.conexiónBD.DatabaseConnection;
import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.ConfiguredExercise;
import com.manageexerciseroutine.model.Exercise;
import com.manageexerciseroutine.model.Routine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ConfiguredExerciseRepositoryImpl implements ConfiguredExerciseRepository {

    Logger logger = Logger.getLogger(ConfiguredExerciseRepositoryImpl.class.getName());

    // Método para encontrar todos los ejercicios configurados por rutinaId
    @Override
    public List<ConfiguredExercise> findAllByRoutineId(int routineId) throws DatabaseOperationException {
        String query = "SELECT ce.orderIndex, ce.repetitions, ce.series, ce.rest, " +
                "e.id as exerciseId, e.name as exerciseName, e.description as exerciseDescription, e.duration as exerciseDuration, e.type as exerciseType, " +
                "r.id as routineId, r.name as routineName, r.description as routineDescription, r.duration as routineDuration, r.difficultyLevel, r.trainingType " +
                "FROM ConfiguredExercises ce " +
                "JOIN Exercises e ON ce.exercise_id = e.id " +
                "JOIN Routines r ON ce.routine_id = r.id " +
                "WHERE r.id = ?";

        List<ConfiguredExercise> configuredExercises = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, routineId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Construir el objeto Exercise
                    Exercise exercise = new Exercise(
                            resultSet.getInt("exerciseId"),
                            resultSet.getString("exerciseName"),
                            resultSet.getString("exerciseDescription"),
                            resultSet.getInt("exerciseDuration"),
                            resultSet.getString("exerciseType"),
                            null  // Omitimos el trainer aquí, ya que no es necesario para esta consulta
                    );

                    // Construir el objeto Routine
                    Routine routine = new Routine(
                            resultSet.getInt("routineId"),
                            resultSet.getString("routineName"),
                            resultSet.getString("routineDescription"),
                            resultSet.getInt("routineDuration"),
                            Routine.DifficultyLevel.valueOf(resultSet.getString("difficultyLevel")),
                            Routine.TrainingType.valueOf(resultSet.getString("trainingType")),
                            null  // Omitimos el trainer aquí para esta consulta
                    );

                    // Construir el objeto ConfiguredExercise
                    ConfiguredExercise configuredExercise = new ConfiguredExercise(
                            resultSet.getInt("orderIndex"),
                            resultSet.getInt("repetitions"),
                            resultSet.getInt("series"),
                            resultSet.getInt("rest"),
                            exercise,  // Asignar el ejercicio completo
                            routine   // Asignar la rutina completa
                    );

                    configuredExercises.add(configuredExercise);
                }
            }
        } catch (SQLException e) {
            logger.info("findAllByRoutineId. Error executing query. error: " + e.getMessage());
            throw new DatabaseOperationException("Error executing query", e);
        }
        return configuredExercises;
    }

    // Guardar un nuevo ejercicio configurado
    @Override
    public void save(ConfiguredExercise configuredExercise) throws DatabaseOperationException {
        String query = "INSERT INTO ConfiguredExercises (orderIndex, repetitions, series, rest, exercise_id, routine_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, configuredExercise.getOrderIndex());
            statement.setInt(2, configuredExercise.getRepetitions());
            statement.setInt(3, configuredExercise.getSeries());
            statement.setInt(4, configuredExercise.getRest());
            statement.setInt(5, configuredExercise.getExercise().getId());  // Asociar con el ID del ejercicio
            statement.setInt(6, configuredExercise.getRoutine().getId());   // Asociar con el ID de la rutina

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.info("save. Error executing query. error: " + e.getMessage());
            throw new DatabaseOperationException("Error executing query", e);
        }
    }

    // Actualizar ejercicio configurado
    @Override
    public void update(ConfiguredExercise exercise) throws DatabaseOperationException {
        String query = "UPDATE ConfiguredExercises SET repetitions = ?, series = ?, rest = ?, orderIndex = ? WHERE " +
                "routine_id = ? AND exercise_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, exercise.getRepetitions());
            statement.setInt(2, exercise.getSeries());
            statement.setInt(3, exercise.getRest());
            statement.setInt(4, exercise.getOrderIndex());
            statement.setInt(5, exercise.getRoutine().getId());
            statement.setInt(6, exercise.getExercise().getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.info("update. Error executing query. error: " + e.getMessage());
            throw new DatabaseOperationException("Error al actualizar el ejercicio configurado", e);
        }
    }


    // Eliminar ejercicio configurado
    @Override
    public void delete(ConfiguredExercise exercise) throws DatabaseOperationException {
        String query = "DELETE FROM ConfiguredExercises WHERE orderIndex = ? AND routine_id = ? AND exercise_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, exercise.getOrderIndex());
            statement.setInt(2, exercise.getRoutine().getId());
            statement.setInt(3, exercise.getExercise().getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.info("delete. Error executing query. error: " + e.getMessage());
            throw new DatabaseOperationException("Error al eliminar el ejercicio configurado", e);
        }
    }

    @Override
    public void deleteByRoutineId(int routineId) throws DatabaseOperationException {
        String query = "DELETE FROM ConfiguredExercises WHERE routine_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, routineId);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.info("deleteByRoutineId. Error executing query. error: " + e.getMessage());
            throw new DatabaseOperationException("Error al eliminar ejercicios configurados para la rutina", e);
        }
    }

}
