package com.manageexerciseroutine.controller;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.Exercise;
import com.manageexerciseroutine.model.Trainer;
import com.manageexerciseroutine.service.ExerciseService;
import com.manageexerciseroutine.service.TrainerService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ExerciseABMController {

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


    private final TrainerService trainerService; // Instancia del servicio de entrenadores

    private final ObservableList<Exercise> exerciseData = FXCollections.observableArrayList();
    private final ExerciseService exerciseService;
    private final int trainerId;  // Recibir trainerId para gestión específica
    Trainer trainer;  // Entrenador actual

    // Constructor con inyección de servicios y trainerId
    public ExerciseABMController(int trainerId) {
        this.trainerId = trainerId;
        this.trainerService = new TrainerService();
        this.exerciseService = new ExerciseService();  // Instancia del servicio de ejercicios
        try {
            this.trainer = trainerService.getTrainerById(trainerId);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo encontrar al entrenador.");
        }
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        durationColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDuration()).asObject());
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));

        loadExercises();
    }

    private void loadExercises() {
        try {
            // Limpiar la lista actual
            exerciseData.clear();

            // Obtener datos actualizados desde el servicio
            List<Exercise> exercises = exerciseService.findAllExercisesByTrainerId(trainerId);
            exerciseData.addAll(exercises);

            // Establecer los datos actualizados en la tabla
            exerciseTable.setItems(exerciseData);
        } catch (DatabaseOperationException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los ejercicios: " + e.getMessage());
        }
    }


    @FXML
    public void handleCreateExercise() throws DatabaseOperationException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/create_exercise_view.fxml"));
            ExerciseController controller = new ExerciseController(trainer);
            loader.setController(controller);

            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Crear Ejercicio");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            if (controller.isUpdated()) {
                // Recargar ejercicios después de crear uno nuevo
                loadExercises();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de creación de ejercicio.");
        }
    }

    @FXML
    public void handleEditExercise() throws DatabaseOperationException {
        Exercise selectedExercise = exerciseTable.getSelectionModel().getSelectedItem();
        if (selectedExercise != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/edit_exercise_view.fxml"));
                ExerciseController controller = new ExerciseController(trainer);
                loader.setController(controller);

                Parent root = loader.load();
                controller.setExerciseToEdit(selectedExercise);

                Stage stage = new Stage();
                stage.setTitle("Editar Ejercicio");
                stage.setScene(new Scene(root));
                stage.showAndWait();

                if (controller.isUpdated()) {
                    // Recargar ejercicios después de editar
                    loadExercises();
                }
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de edición.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selección vacía", "Por favor selecciona un ejercicio para editar.");
        }
    }



    @FXML
    public void handleDeleteExercise() {
        Exercise selectedExercise = exerciseTable.getSelectionModel().getSelectedItem();
        if (selectedExercise != null) {
            Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION, "Confirmación", "¿Estás seguro de que deseas eliminar este ejercicio?");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    exerciseService.deleteExercise(selectedExercise); // Eliminar de la base de datos
                    exerciseData.remove(selectedExercise); // Remover del ObservableList
                    exerciseTable.refresh(); // Refrescar la tabla
                    showAlert(Alert.AlertType.INFORMATION, "Éxito", "¡Ejercicio eliminado!");
                } catch (DatabaseOperationException e) {
                    if (e.getMessage().contains("No se puede eliminar el ejercicio porque está asociado a una rutina")) {
                        showAlert(Alert.AlertType.ERROR, "Error", "No se puede eliminar el ejercicio porque está asociado a una rutina");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "No se pudo guardar el ejercicio: " + e.getMessage());
                    }
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selección vacía", "Por favor selecciona un ejercicio para eliminar.");
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
            exercise.setDescription("Sample description");  // Ajustar descripción en un formulario más completo
            exercise.setDuration(10);  // Duración por defecto
            exercise.setType("Strength");  // Tipo por defecto, ajustable en el formulario
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