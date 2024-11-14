package com.manageexerciseroutine.controller;

import com.manageexerciseroutine.model.ConfiguredExercise;
import com.manageexerciseroutine.model.Exercise;
import com.manageexerciseroutine.model.Routine;
import com.manageexerciseroutine.service.ExerciseService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class ConfiguredExerciseController {

    @FXML
    private ComboBox<Exercise> exerciseComboBox;

    @FXML
    private TextField orderField;

    @FXML
    private TextField repetitionsField;

    @FXML
    private TextField seriesField;

    @FXML
    private TextField restField;

    private ConfiguredExercise configuredExercise;
    private final ExerciseService exerciseService = new ExerciseService();

    private Routine routine;
    private int trainerId;

    public ConfiguredExerciseController(int trainerId, Routine routine) {
        this.trainerId = trainerId;
        this.routine = routine;
    }

    @FXML
    public void initialize() {
        // Cargar ejercicios disponibles en el ComboBox
        List<Exercise> exercises = exerciseService.findAllExercisesByTrainerId(trainerId);  // Método que debe traer los ejercicios disponibles
        exerciseComboBox.setItems(FXCollections.observableArrayList(exercises));
    }

    @FXML
    public void handleSaveConfiguredExercise() {
        try {
            Exercise selectedExercise = exerciseComboBox.getValue();
            int order = Integer.parseInt(orderField.getText());
            int repetitions = Integer.parseInt(repetitionsField.getText());
            int series = Integer.parseInt(seriesField.getText());
            int rest = Integer.parseInt(restField.getText());

            // Crear el objeto ConfiguredExercise
            configuredExercise = new ConfiguredExercise(order, repetitions, series, selectedExercise, this.routine, rest);  // Null se reemplaza por la rutina en el controlador de la rutina

            // Cerrar la ventana de configuración
            Stage stage = (Stage) exerciseComboBox.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Entrada", "Por favor, ingresa valores válidos.");
        }
    }

    public ConfiguredExercise getConfiguredExercise() {
        return configuredExercise;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
