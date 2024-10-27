package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.configuration.DatabaseConnection;
import com.manageexerciseroutine.model.Trainer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainerRepositoryImpl implements TrainerRepository {

    @Override
    public void save(Trainer trainer) throws SQLException {
        String query = "INSERT INTO Trainers (name, email, password, specialty, biography) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, trainer.getName());
            statement.setString(2, trainer.getEmail());
            statement.setString(3, trainer.getPassword());
            statement.setString(4, trainer.getSpecialty());
            statement.setString(5, trainer.getBiography());
            statement.executeUpdate();
        }
    }

    @Override
    public List<Trainer> findAll() throws SQLException {
        String query = "SELECT * FROM Trainers";
        List<Trainer> trainers = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Trainer trainer = new Trainer(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("specialty"),
                        resultSet.getString("biography")
                );
                trainers.add(trainer);
            }
        }
        return trainers;
    }

    @Override
    public Trainer findById(int id) throws SQLException {
        String query = "SELECT * FROM Trainers WHERE id = ?";
        Trainer trainer = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    trainer = new Trainer(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getString("specialty"),
                            resultSet.getString("biography")
                    );
                }
            }
        }
        return trainer;
    }

    @Override
    public void update(Trainer trainer) throws SQLException {
        String query = "UPDATE Trainers SET name = ?, email = ?, password = ?, specialty = ?, biography = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, trainer.getName());
            statement.setString(2, trainer.getEmail());
            statement.setString(3, trainer.getPassword());
            statement.setString(4, trainer.getSpecialty());
            statement.setString(5, trainer.getBiography());
            statement.setInt(6, trainer.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM Trainers WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public List<Trainer> findBySpecialty(String specialty) throws SQLException {
        String query = "SELECT * FROM Trainers WHERE specialty = ?";
        List<Trainer> trainers = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, specialty);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Trainer trainer = new Trainer(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getString("specialty"),
                            resultSet.getString("biography")
                    );
                    trainers.add(trainer);
                }
            }
        }
        return trainers;
    }

    @Override
    public Trainer findByEmailAndPassword(String email, String password) throws SQLException {
        String query = "SELECT * FROM Trainers WHERE email = ? AND password = ?";
        Trainer trainer = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    trainer = new Trainer(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getString("specialty"),
                            resultSet.getString("biography")
                    );
                }
            }
        }
        return trainer;
    }
}
