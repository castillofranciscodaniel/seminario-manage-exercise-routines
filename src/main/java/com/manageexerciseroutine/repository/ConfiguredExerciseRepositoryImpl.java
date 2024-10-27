package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.configuration.DatabaseConnection;
import com.manageexerciseroutine.model.ConfiguredExercise;
import com.manageexerciseroutine.model.Exercise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConfiguredExerciseRepositoryImpl implements ConfiguredExerciseRepository {

    public ConfiguredExerciseRepositoryImpl() {
    }

    @Override
    public List<ConfiguredExercise> findByRoutineId(int routineId) throws SQLException {
        String query = "SELECT ce.orderIndex, ce.repetitions, ce.series, " +
                "e.id, e.name, e.description, e.duration, e.type " +
                "FROM ConfiguredExercises ce " +
                "JOIN Exercises e ON ce.exercise_id = e.id " +
                "WHERE ce.routine_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, routineId);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<ConfiguredExercise> exercises = new ArrayList<>();
                while (resultSet.next()) {
                    // Crear objeto Exercise con todos los campos
                    Exercise exercise = new Exercise(
                            resultSet.getInt("id"),               // ID del ejercicio
                            resultSet.getString("name"),           // Nombre
                            resultSet.getString("description"),    // Descripción
                            resultSet.getInt("duration"),          // Duración
                            resultSet.getString("type")            // Tipo
                    );

                    // Crear objeto ConfiguredExercise
                    ConfiguredExercise configuredExercise = new ConfiguredExercise(
                            resultSet.getInt("orderIndex"),        // Orden en la rutina
                            resultSet.getInt("repetitions"),       // Repeticiones
                            resultSet.getInt("series"),            // Series
                            exercise,                             // Objeto Exercise completo
                            null                                  // Rutina, puede ser null si no es necesaria aquí
                    );
                    exercises.add(configuredExercise);
                }
                return exercises;
            }
        }
    }
}
