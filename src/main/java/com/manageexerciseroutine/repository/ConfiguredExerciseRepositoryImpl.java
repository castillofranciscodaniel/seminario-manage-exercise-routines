package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.configuration.DatabaseConnection;
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

public class ConfiguredExerciseRepositoryImpl implements ConfiguredExerciseRepository {

    // Método para encontrar todos los ejercicios configurados por rutinaId
    @Override
    public List<ConfiguredExercise> findAllByRoutineId(int routineId) throws DatabaseOperationException {
        String query = "SELECT ce.orderIndex, ce.repetitions, ce.series, " +
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
                            exercise,  // Asignar el ejercicio completo
                            routine   // Asignar la rutina completa
                    );

                    configuredExercises.add(configuredExercise);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error executing query", e);
        }
        return configuredExercises;
    }

    // Guardar un nuevo ejercicio configurado
    @Override
    public void save(ConfiguredExercise configuredExercise) throws DatabaseOperationException {
        String query = "INSERT INTO ConfiguredExercises (orderIndex, repetitions, series, exercise_id, routine_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, configuredExercise.getOrderIndex());
            statement.setInt(2, configuredExercise.getRepetitions());
            statement.setInt(3, configuredExercise.getSeries());
            statement.setInt(4, configuredExercise.getExercise().getId());  // Asociar con el ID del ejercicio
            statement.setInt(5, configuredExercise.getRoutine().getId());   // Asociar con el ID de la rutina

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error executing query", e);
        }
    }

    // Actualizar ejercicio configurado
    @Override
    public void update(ConfiguredExercise configuredExercise) throws DatabaseOperationException {
        String query = "UPDATE ConfiguredExercises SET orderIndex = ?, repetitions = ?, series = ?, exercise_id = ?, routine_id = ? " +
                "WHERE orderIndex = ? AND routine_id = ? AND exercise_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, configuredExercise.getOrderIndex());
            statement.setInt(2, configuredExercise.getRepetitions());
            statement.setInt(3, configuredExercise.getSeries());
            statement.setInt(4, configuredExercise.getExercise().getId());  // Asociar con el ID del ejercicio
            statement.setInt(5, configuredExercise.getRoutine().getId());   // Asociar con el ID de la rutina
            statement.setInt(6, configuredExercise.getOrderIndex());
            statement.setInt(7, configuredExercise.getRoutine().getId());
            statement.setInt(8, configuredExercise.getExercise().getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error executing query", e);
        }
    }

    // Eliminar ejercicio configurado
    @Override
    public void delete(ConfiguredExercise configuredExercise) throws DatabaseOperationException {
        String query = "DELETE FROM ConfiguredExercises WHERE orderIndex = ? AND routine_id = ? AND exercise_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, configuredExercise.getOrderIndex());
            statement.setInt(2, configuredExercise.getRoutine().getId());
            statement.setInt(3, configuredExercise.getExercise().getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error executing query", e);
        }
    }
}
