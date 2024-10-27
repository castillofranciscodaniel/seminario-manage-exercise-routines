package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.configuration.DatabaseConnection;
import com.manageexerciseroutine.model.Subscriber;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubscriberRepositoryImpl implements SubscriberRepository {

    @Override
    public void save(Subscriber subscriber) throws SQLException {
        String query = "INSERT INTO Subscribers (name, email, password, registrationDate) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, subscriber.getName());
            statement.setString(2, subscriber.getEmail());
            statement.setString(3, subscriber.getPassword());
            statement.setDate(4, new Date(subscriber.getRegistrationDate().getTime()));
            statement.executeUpdate();
        }
    }

    @Override
    public List<Subscriber> findAll() throws SQLException {
        String query = "SELECT * FROM Subscribers";
        List<Subscriber> subscribers = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Subscriber subscriber = new Subscriber(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getDate("registrationDate")
                );
                subscribers.add(subscriber);
            }
        }
        return subscribers;
    }

    @Override
    public Subscriber findById(int id) throws SQLException {
        String query = "SELECT * FROM Subscribers WHERE id = ?";
        Subscriber subscriber = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    subscriber = new Subscriber(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getDate("registrationDate")
                    );
                }
            }
        }
        return subscriber;
    }

    @Override
    public void update(Subscriber subscriber) throws SQLException {
        String query = "UPDATE Subscribers SET name = ?, email = ?, password = ?, registrationDate = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, subscriber.getName());
            statement.setString(2, subscriber.getEmail());
            statement.setString(3, subscriber.getPassword());
            statement.setDate(4, new Date(subscriber.getRegistrationDate().getTime()));
            statement.setInt(5, subscriber.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM Subscribers WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public List<Subscriber> findByRegistrationDate(String date) throws SQLException {
        String query = "SELECT * FROM Subscribers WHERE registrationDate = ?";
        List<Subscriber> subscribers = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, Date.valueOf(date));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Subscriber subscriber = new Subscriber(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getDate("registrationDate")
                    );
                    subscribers.add(subscriber);
                }
            }
        }
        return subscribers;
    }
}
