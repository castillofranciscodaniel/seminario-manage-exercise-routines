package com.manageexerciseroutine.controller;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.ConfiguredExercise;
import com.manageexerciseroutine.model.Routine;
import com.manageexerciseroutine.model.Trainer;
import com.manageexerciseroutine.service.RoutineService;
import com.manageexerciseroutine.service.TrainerService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RoutineController {

    private final RoutineService routineService = new RoutineService();
    private final TrainerService trainerService = new TrainerService();
    private Trainer trainer;  // Cambiado para cargar el objeto completo desde el ID
    private Routine routineToEdit;

    @FXML
    private TextField nameField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField durationField;

    @FXML
    private ComboBox<String> difficultyLevelComboBox;

    @FXML
    private ComboBox<String> trainingTypeComboBox;

    @FXML
    private TableView<ConfiguredExercise> configuredExercisesTable;

    public RoutineController(int trainerId) {
        try {
            this.trainer = trainerService.getTrainerById(trainerId); // Cargar el entrenador usando el ID
        } catch (DatabaseOperationException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar los datos del entrenador.");
        }
    }

    public RoutineController(int trainerId, Routine routineToEdit) {
        this(trainerId);
        this.routineToEdit = routineToEdit;
    }

    @FXML
    public void initialize() {
        difficultyLevelComboBox.setItems(FXCollections.observableArrayList("Beginner", "Intermediate", "Advanced"));
        trainingTypeComboBox.setItems(FXCollections.observableArrayList("Strength", "Cardio", "Yoga", "Full-body"));

        if (routineToEdit != null) {
            nameField.setText(routineToEdit.getName());
            descriptionField.setText(routineToEdit.getDescription());
            durationField.setText(String.valueOf(routineToEdit.getDuration()));
            difficultyLevelComboBox.setValue(routineToEdit.getDifficultyLevel());
            trainingTypeComboBox.setValue(routineToEdit.getTrainingType());
        }
    }

    @FXML
    private void handleSaveRoutine() {
        try {
            if (routineToEdit == null) {
                routineToEdit = new Routine();
            }

            routineToEdit.setName(nameField.getText());
            routineToEdit.setDescription(descriptionField.getText());
            routineToEdit.setDuration(Integer.parseInt(durationField.getText()));
            routineToEdit.setDifficultyLevel(difficultyLevelComboBox.getValue());
            routineToEdit.setTrainingType(trainingTypeComboBox.getValue());
            routineToEdit.setTrainer(trainer);

            if (routineToEdit.getId() == 0) {
                routineService.saveRoutine(routineToEdit);
            } else {
                routineService.updateRoutine(routineToEdit);
            }

            showAlert(Alert.AlertType.INFORMATION, "Éxito", "¡Rutina guardada exitosamente!");
            closeWindow();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo guardar la rutina.");
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}




