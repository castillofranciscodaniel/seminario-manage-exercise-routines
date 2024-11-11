package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.configuration.DatabaseConnection;
import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.Routine;
import com.manageexerciseroutine.model.Subscriber;
import com.manageexerciseroutine.model.Subscription;
import com.manageexerciseroutine.model.Trainer;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    // SubscriptionRepository.java
    public List<Subscription> findActiveSubscriptionsByUserId(int userId) throws DatabaseOperationException {
        String query = "SELECT s.id, s.startDate, s.endDate, s.status, " +
                "r.id AS routineId, r.name AS routineName, r.description, r.duration, " +
                "r.difficultyLevel, r.trainingType, " +
                "t.id AS trainerId, t.name AS trainerName, t.email AS trainerEmail " +
                "FROM Subscriptions s " +
                "JOIN Routines r ON s.routine_id = r.id " +
                "JOIN Trainers t ON r.trainer_id = t.id " +
                "WHERE s.user_id = ? AND s.status = ?";  // Solo suscripciones activas

        List<Subscription> subscriptions = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setString(2, Subscription.Status.ACTIVE.toString());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Trainer trainer = new Trainer(
                            resultSet.getString("trainerName")
                    );

                    Routine routine = new Routine(
                            resultSet.getInt("routineId"),
                            resultSet.getString("routineName"),
                            resultSet.getString("description"),
                            resultSet.getInt("duration"),
                            resultSet.getString("difficultyLevel"),
                            resultSet.getString("trainingType"),
                            trainer
                    );

                    Subscription subscription = new Subscription(
                            resultSet.getInt("id"),
                            resultSet.getDate("startDate"),
                            resultSet.getDate("endDate"),
                            Subscription.Status.valueOf(resultSet.getString("status")),
                            new Subscriber(userId),  // Suponiendo que solo se necesita el ID
                            routine
                    );

                    subscriptions.add(subscription);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error al recuperar suscripciones activas para el usuario.", e);
        }
        return subscriptions;
    }


    @Override
    public void save(Subscription subscription) throws DatabaseOperationException {
        String query = "INSERT INTO Subscriptions (startDate, status, user_id, routine_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, new java.sql.Date(subscription.getStartDate().getTime()));
            statement.setString(2, subscription.getStatus().toString());
            statement.setInt(3, subscription.getSubscriber().getId());  // Ahora es id
            statement.setInt(4, subscription.getRoutine().getId());  // Ahora es
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error executing query", e);
        }
    }

    @Override
    public void update(Subscription subscription) throws DatabaseOperationException {
        String query = "UPDATE Subscriptions SET startDate = ?, endDate = ?, status = ?, subscriber_id = ?, routine_id = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, new java.sql.Date(subscription.getStartDate().getTime()));
            statement.setDate(2, new java.sql.Date(subscription.getEndDate().getTime()));
            statement.setString(3, subscription.getStatus().toString());
            statement.setInt(4, subscription.getSubscriber().getId());  // Ahora es id
            statement.setInt(5, subscription.getRoutine().getId());  // Ahora es id
            statement.setInt(6, subscription.getId());  // Ahora es id
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error executing query", e);
        }
    }

    @Override
    public void markSubscriptionAsEnded(int subscriptionId) throws DatabaseOperationException {
        String query = "UPDATE Subscriptions SET endDate = ?, status = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, new Date(Calendar.getInstance().getTime().getTime()));  // Establece la fecha de finalización actual
            statement.setString(2, Subscription.Status.ENDED.toString());
            statement.setInt(3, subscriptionId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error executing query", e);
        }
    }
}
