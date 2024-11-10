package com.manageexerciseroutine.controller;

import com.manageexerciseroutine.model.Subscriber;
import com.manageexerciseroutine.model.Trainer;
import com.manageexerciseroutine.service.SubscriberService;
import com.manageexerciseroutine.service.TrainerService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegisterController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private CheckBox subscriberCheckBox;
    @FXML
    private CheckBox trainerCheckBox;
    @FXML
    private TextField specialtyField;
    @FXML
    private TextArea biographyField;
    @FXML
    private Label specialtyField_label;

    private final SubscriberService subscriberService;
    private final TrainerService trainerService;

    public RegisterController(SubscriberService subscriberService, TrainerService trainerService) {
        this.subscriberService = subscriberService;
        this.trainerService = trainerService;
    }

    @FXML
    public void initialize() {
        // Controla que solo se pueda seleccionar un tipo de usuario a la vez
        subscriberCheckBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) trainerCheckBox.setSelected(false);
        });

        trainerCheckBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) subscriberCheckBox.setSelected(false);
        });

        // Muestra u oculta los campos de Entrenador según la selección
        specialtyField.setVisible(false);
        specialtyField_label.setVisible(false);
        trainerCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            specialtyField.setVisible(newVal);
            specialtyField_label.setVisible(newVal);
        });
    }

    @FXML
    public void handleRegister() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String biography = biographyField.getText();
        System.out.println("name: " + name + ", email: " + email + ", password: " + password + ", biography: " + biography);

        if (subscriberCheckBox.isSelected()) {
            Subscriber subscriber = new Subscriber(name, email, password, biography);
            subscriberService.registerSubscriber(subscriber);
            showAlert(Alert.AlertType.INFORMATION, "Registro Exitoso", "Suscriptor registrado con éxito.");
        } else if (trainerCheckBox.isSelected()) {
            String specialty = specialtyField.getText();
            Trainer trainer = new Trainer(name, email, password, specialty, biography);
            trainerService.registerTrainer(trainer);
            showAlert(Alert.AlertType.INFORMATION, "Registro Exitoso", "Entrenador registrado con éxito.");
        } else {
            showAlert(Alert.AlertType.WARNING, "Tipo de Usuario", "Por favor selecciona un tipo de usuario.");
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
