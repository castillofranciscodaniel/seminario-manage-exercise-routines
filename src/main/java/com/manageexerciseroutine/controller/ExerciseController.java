package com.manageexerciseroutine.controller;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.Exercise;
import com.manageexerciseroutine.model.Trainer;
import com.manageexerciseroutine.service.ExerciseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Data;
import lombok.Getter;

import java.util.Optional;
import java.util.logging.Logger;

@Data
public class ExerciseController {

    Logger logger = Logger.getLogger(ExerciseController.class.getName());


    @FXML
    private TableView<Exercise> exerciseTable;

    @FXML
    private TableColumn<Exercise, String> nameColumn;

    @FXML
    private TableColumn<Exercise, String> descriptionColumn;

    @FXML
    private TableColumn<Exercise, Integer> durationColumn;

    @FXML
    private TableColumn<Exercise, String> typeColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField durationField;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private TableColumn<Exercise, String> trainerColumn; // Columna para mostrar el nombre del Trainer

    @Getter
    private Exercise createdExercise; // Variable para almacenar el ejercicio creado

    private final ObservableList<Exercise> exerciseData = FXCollections.observableArrayList();

    private final ExerciseService exerciseService;

    private Trainer trainer;

    // Constructor con inyección de servicios y trainerId
    public ExerciseController(Trainer trainer) {
        this.exerciseService = new ExerciseService();
        this.trainer = trainer;
    }

    @FXML
    public void initialize() throws DatabaseOperationException {

        typeComboBox.setItems(FXCollections.observableArrayList(
                "Strength", "Yoga", "Cardio", "Core", "Full-body"
        ));
    }

    @FXML
    public void handleSaveExercise() {
        try {
            String name = nameField.getText();
            String description = descriptionField.getText();
            int duration = Integer.parseInt(durationField.getText());
            String type = typeComboBox.getValue();

            if (type == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Por favor selecciona un tipo de ejercicio.");
                return;
            }

            createdExercise = new Exercise();  // Crear el objeto de ejercicio
            createdExercise.setName(name);
            createdExercise.setDescription(description);
            createdExercise.setDuration(duration);
            createdExercise.setType(type);
            createdExercise.setTrainer(trainer);

            exerciseService.saveExercise(createdExercise); // Guardar en la base de datos
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "¡Ejercicio creado exitosamente!");

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Duración inválida. Por favor ingresa un número.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo guardar el ejercicio: " + e.getMessage());
        }
    }

    // Editar ejercicio existente
    @FXML
    public void handleEditExercise() throws DatabaseOperationException {
        Exercise selectedExercise = exerciseTable.getSelectionModel().getSelectedItem();
        if (selectedExercise != null) {
            Exercise updatedExercise = showExerciseDialog(selectedExercise);
            if (updatedExercise != null) {
                // Actualiza el ejercicio en la base de datos
                exerciseService.updateExercise(updatedExercise);
                // Refresca la tabla de ejercicios
                exerciseTable.refresh();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an exercise to edit.");
        }
    }

    // Eliminar ejercicio seleccionado
    @FXML
    public void handleDeleteExercise() throws DatabaseOperationException {
        Exercise selectedExercise = exerciseTable.getSelectionModel().getSelectedItem();
        if (selectedExercise != null) {
            Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION, "Confirm Delete", "Are you sure you want to delete this exercise?");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Elimina el ejercicio de la base de datos
                exerciseService.deleteExercise(selectedExercise);
                // Remueve el ejercicio de la tabla
                exerciseData.remove(selectedExercise);
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an exercise to delete.");
        }
    }

    // Mostrar el diálogo para crear/editar un ejercicio
    private Exercise showExerciseDialog(Exercise exercise) {
        TextInputDialog dialog = new TextInputDialog(exercise.getName());
        dialog.setTitle("Exercise Dialog");
        dialog.setHeaderText("Enter exercise details:");
        dialog.setContentText("Exercise Name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            exercise.setName(result.get());
            exercise.setDescription("Sample description");  // Ajustar la descripción según sea necesario
            exercise.setDuration(10);  // Duración por defecto, ajustable
            exercise.setType("Strength");  // Tipo por defecto
            return exercise;
        }
        return null;
    }

    // Mostrar alertas
    private Optional<ButtonType> showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait();
    }
}