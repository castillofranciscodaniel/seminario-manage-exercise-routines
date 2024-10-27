package com.manageexerciseroutine.controller;

import com.manageexerciseroutine.model.Trainer;
import com.manageexerciseroutine.model.Subscriber;
import com.manageexerciseroutine.service.TrainerService;
import com.manageexerciseroutine.service.SubscriberService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.List;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private final TrainerService trainerService;
    private final SubscriberService subscriberService;

    public LoginController(TrainerService trainerService, SubscriberService subscriberService) {
        this.trainerService = trainerService;
        this.subscriberService = subscriberService;
    }

    @FXML
    public void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter both email and password");
            return;
        }

        try {
            // Intentar iniciar sesión como entrenador
            List<Trainer> trainers = trainerService.getAllTrainers();
            for (Trainer trainer : trainers) {
                if (trainer.getEmail().equals(email) && trainer.getPassword().equals(password)) {
                    showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome Trainer: " + trainer.getName());
                    return;
                }
            }

            // Intentar iniciar sesión como suscriptor
            List<Subscriber> subscribers = subscriberService.getAllSubscribers();
            for (Subscriber subscriber : subscribers) {
                if (subscriber.getEmail().equals(email) && subscriber.getPassword().equals(password)) {
                    showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome Subscriber: " + subscriber.getName());
                    return;
                }
            }

            // Si no se encuentra el usuario
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error accessing the database.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
