package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.conexiónBD.DatabaseConnection;
import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.Trainer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainerRepositoryImpl implements TrainerRepository {

    @Override
    public void save(Trainer trainer) throws DatabaseOperationException {
        String query = "INSERT INTO Trainers (name, email, password, specialty, biography) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, trainer.getName());
            statement.setString(2, trainer.getEmail());
            statement.setString(3, trainer.getPassword());
            statement.setString(4, trainer.getSpecialty());
            statement.setString(5, trainer.getBiography());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error executing query", e);
        }
    }

    @Override
    public List<Trainer> findAll() throws DatabaseOperationException {
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
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error executing query", e);
        }
        return trainers;
    }

    @Override
    public Trainer findById(int id) throws DatabaseOperationException {
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
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error executing query", e);
        }
        return trainer;
    }

    @Override
    public void update(Trainer trainer) throws DatabaseOperationException {
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
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error executing query", e);
        }
    }

    @Override
    public void delete(int id) throws DatabaseOperationException {
        String query = "DELETE FROM Trainers WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error executing query", e);
        }
    }

    @Override
    public List<Trainer> findBySpecialty(String specialty) throws DatabaseOperationException {
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
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error executing query", e);
        }
        return trainers;
    }

    @Override
    public Trainer findByEmailAndPassword(String email, String password) throws DatabaseOperationException {
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
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error executing query", e);
        }
        return trainer;
    }
}
