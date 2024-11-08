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

    private Exercise exerciseToEdit; // Ejercicio a editar, si es aplicable


    private final ObservableList<Exercise> exerciseData = FXCollections.observableArrayList();

    private final ExerciseService exerciseService;

    private Trainer trainer;

    @Getter
    private boolean updated = false;

    public Exercise getExercise() {
        return exerciseToEdit;
    }

    // Constructor con inyección de servicios y trainerId
    public ExerciseController(Trainer trainer) {
        this.exerciseService = new ExerciseService();
        this.trainer = trainer;
    }

    public void setExerciseToEdit(Exercise exercise) {
        if (nameField != null && descriptionField != null && durationField != null && typeComboBox != null) {
            this.exerciseToEdit = exercise;
            nameField.setText(exercise.getName());
            descriptionField.setText(exercise.getDescription());
            durationField.setText(String.valueOf(exercise.getDuration()));
            typeComboBox.setValue(exercise.getType());
        } else {
            System.out.println("Campos no inicializados aún");
        }
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
            // Capturar datos de la interfaz
            String name = nameField.getText();
            String description = descriptionField.getText();
            int duration = Integer.parseInt(durationField.getText());
            String type = typeComboBox.getValue();

            if (type == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Por favor selecciona un tipo de ejercicio.");
                return;
            }

            if (exerciseToEdit == null) {
                exerciseToEdit = new Exercise();
            }

            exerciseToEdit.setName(name);
            exerciseToEdit.setDescription(description);
            exerciseToEdit.setDuration(duration);
            exerciseToEdit.setType(type);
            exerciseToEdit.setTrainer(trainer);

            exerciseService.saveExercise(exerciseToEdit);

            updated = true;  // Marcar como actualizado
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "¡Ejercicio guardado exitosamente!");

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Duración inválida. Por favor ingresa un número.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo guardar el ejercicio: " + e.getMessage());
        }
    }


    private void populateExerciseData(Exercise exercise) {
        exercise.setName(nameField.getText());
        exercise.setDescription(descriptionField.getText());
        exercise.setDuration(Integer.parseInt(durationField.getText()));
        exercise.setType(typeComboBox.getValue());
        exercise.setTrainer(trainer);
    }


    @FXML
    public void handleCreateExercise() {
        Exercise newExercise = new Exercise();
        populateExerciseData(newExercise);
        exerciseService.saveExercise(newExercise);
        exerciseData.add(newExercise);
        exerciseTable.refresh();
        showAlert(Alert.AlertType.INFORMATION, "Éxito", "¡Ejercicio creado exitosamente!");
    }

    @FXML
    public void handleEditExercise() {
        Exercise selectedExercise = exerciseTable.getSelectionModel().getSelectedItem();
        if (selectedExercise != null) {
            populateExerciseData(selectedExercise);
            exerciseService.saveExercise(selectedExercise);
            exerciseTable.refresh();
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "¡Ejercicio actualizado exitosamente!");
        } else {
            showAlert(Alert.AlertType.WARNING, "Selección vacía", "Por favor selecciona un ejercicio para editar.");
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