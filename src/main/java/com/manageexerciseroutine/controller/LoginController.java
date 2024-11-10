package com.manageexerciseroutine.controller;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.exeptions.UnauthorizedAccessException;
import com.manageexerciseroutine.model.Subscriber;
import com.manageexerciseroutine.model.Trainer;
import com.manageexerciseroutine.repository.SubscriptionRepository;
import com.manageexerciseroutine.repository.SubscriptionRepositoryImpl;
import com.manageexerciseroutine.service.SubscriberService;
import com.manageexerciseroutine.service.SubscriptionService;
import com.manageexerciseroutine.service.TrainerService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

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
                redirectToTrainerMenu(trainer.getId());
                return;
            }

            // Intentar iniciar sesión como suscriptor
            Subscriber subscriber = subscriberService.loginSubscriber(email, password);
            if (subscriber != null) {
                redirectToSubscriptions(subscriber.getId());
                return;
            }

            // Si no se encuentra el usuario
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password");
            throw new UnauthorizedAccessException("Invalid email or password");

        } catch (DatabaseOperationException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error accessing the database.");
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void redirectToTrainerMenu(int trainerId) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/trainer_menu_view.fxml"));

        // Crear el controlador manualmente con el trainerId
        TrainerMenuController controller = new TrainerMenuController(trainerId);
        loader.setController(controller);

        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Menú del entrenador");
        stage.setScene(new Scene(root, 400, 200));
        stage.show();
    }


    private void redirectToSubscriptions(int userId) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/subscriptions_view.fxml"));

        // Crear el repositorio y el servicio
        SubscriptionRepository subscriptionRepository = new SubscriptionRepositoryImpl();
        SubscriptionService subscriptionService = new SubscriptionService(subscriptionRepository);

        // Crear el controlador manualmente con el userId y el SubscriptionService
        SubscriptionsController controller = new SubscriptionsController(subscriptionService, userId);
        loader.setController(controller);  // Asignar el controlador manualmente

        // Cargar la vista
        Parent root = loader.load();

        // Mostrar la ventana
        Stage stage = new Stage();
        stage.setTitle("Mis suscripciones");
        stage.setScene(new Scene(root, 600, 400));
        stage.setMaximized(true); // Maximizar la ventana
        stage.show();
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Método para abrir la ventana de registro
    @FXML
    public void openRegisterView() {
        try {
            URL url = getClass().getResource("/register_view.fxml");
            System.out.println("holi " + url);
            FXMLLoader loader = new FXMLLoader(url);

            RegisterController controller = new RegisterController(subscriberService, trainerService);
            loader.setController(controller);
            Parent root = loader.load();


            Stage registerStage = new Stage();
            registerStage.setTitle("Registro de Usuario");
            registerStage.setScene(new Scene(root));
            registerStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de registro.");
        }
    }
}
