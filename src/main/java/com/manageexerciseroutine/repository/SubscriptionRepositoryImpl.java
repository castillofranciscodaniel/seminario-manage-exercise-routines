package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.configuration.DatabaseConnection;
import com.manageexerciseroutine.model.Routine;
import com.manageexerciseroutine.model.Subscriber;
import com.manageexerciseroutine.model.Subscription;
import com.manageexerciseroutine.model.Trainer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    public List<Subscription> findBySubscriberId(int subscriberId) throws SQLException {
        String query = "SELECT s.id, s.startDate, s.endDate, s.status, " +
                "sub.id as id, sub.name as subscriberName, sub.email as subscriberEmail, sub.registrationDate, " +
                "r.id as id, r.name, r.description, r.duration, r.difficultyLevel, r.trainingType, " +
                "t.id as id, t.name as trainerName, t.email as trainerEmail, t.specialty, t.biography " +
                "FROM Subscriptions s " +
                "JOIN Subscribers sub ON s.user_id = sub.id " +
                "JOIN Routines r ON s.routine_id = r.id " +
                "JOIN Trainers t ON r.trainer_id = t.id " +
                "WHERE s.user_id = ?";

        List<Subscription> subscriptions = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, subscriberId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Construir el objeto Subscriber
                    Subscriber subscriber = new Subscriber(
                            resultSet.getInt("id"),  // Ahora es id
                            resultSet.getString("subscriberName"),
                            resultSet.getString("subscriberEmail"),
                            resultSet.getDate("registrationDate")
                    );

                    // Construir el objeto Trainer
                    Trainer trainer = new Trainer(
                            resultSet.getInt("id"),  // Ahora es id
                            resultSet.getString("trainerName"),
                            resultSet.getString("trainerEmail"),
                            resultSet.getString("specialty"),
                            resultSet.getString("biography")
                    );

                    // Construir el objeto Routine
                    Routine routine = new Routine(
                            resultSet.getInt("id"),  // Ahora es id
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getInt("duration"),
                            resultSet.getString("difficultyLevel"),
                            resultSet.getString("trainingType"),
                            trainer  // Aquí usamos el objeto Trainer
                    );

                    // Construir el objeto Subscription
                    Subscription subscription = new Subscription(
                            resultSet.getInt("id"),  // Ahora es id
                            resultSet.getDate("startDate"),
                            resultSet.getDate("endDate"),
                            resultSet.getString("status"),
                            subscriber,
                            routine
                    );
                    subscriptions.add(subscription);
                }
            }
        }
        return subscriptions;
    }

    @Override
    public void save(Subscription subscription) throws SQLException {
        String query = "INSERT INTO Subscriptions (startDate, endDate, status, subscriber_id, routine_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, new java.sql.Date(subscription.getStartDate().getTime()));
            statement.setDate(2, new java.sql.Date(subscription.getEndDate().getTime()));
            statement.setString(3, subscription.getStatus());
            statement.setInt(4, subscription.getSubscriber().getId());  // Ahora es id
            statement.setInt(5, subscription.getRoutine().getId());  // Ahora es id
            statement.executeUpdate();
        }
    }

    @Override
    public void update(Subscription subscription) throws SQLException {
        String query = "UPDATE Subscriptions SET startDate = ?, endDate = ?, status = ?, subscriber_id = ?, routine_id = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, new java.sql.Date(subscription.getStartDate().getTime()));
            statement.setDate(2, new java.sql.Date(subscription.getEndDate().getTime()));
            statement.setString(3, subscription.getStatus());
            statement.setInt(4, subscription.getSubscriber().getId());  // Ahora es id
            statement.setInt(5, subscription.getRoutine().getId());  // Ahora es id
            statement.setInt(6, subscription.getId());  // Ahora es id
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Subscription subscription) throws SQLException {
        String query = "DELETE FROM Subscriptions WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, subscription.getId());  // Ahora es id
            statement.executeUpdate();
        }
    }
}
