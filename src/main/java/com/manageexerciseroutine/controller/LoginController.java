package com.manageexerciseroutine.controller;

import com.manageexerciseroutine.model.Trainer;
import com.manageexerciseroutine.model.Subscriber;
import com.manageexerciseroutine.model.Routine;
import com.manageexerciseroutine.model.Subscription;
import com.manageexerciseroutine.service.TrainerService;
import com.manageexerciseroutine.service.SubscriberService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
            Trainer trainer = trainerService.loginTrainer(email, password);
            if (trainer != null) {
                redirectToRoutines();
                return;
            }

            // Intentar iniciar sesión como suscriptor
            Subscriber subscriber = subscriberService.loginSubscriber(email, password);
            if (subscriber != null) {
                redirectToSubscriptions();
                return;
            }

            // Si no se encuentra el usuario
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error accessing the database.");
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void redirectToRoutines() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/routines_view.fxml"));

        Parent root = loader.load();  // Cargar la vista sin necesidad de pasar rutinas
        Stage stage = new Stage();
        stage.setTitle("My Routines");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    private void redirectToSubscriptions() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/subscriptions_view.fxml"));

        Parent root = loader.load();  // Cargar la vista sin necesidad de pasar suscripciones
        Stage stage = new Stage();
        stage.setTitle("My Subscriptions");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
