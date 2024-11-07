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
import java.net.URL;
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
    public void initialize() throws SQLException {
        // Inicializar columnas de la tabla
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        durationColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDuration()).asObject());
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));

        // Cargar ejercicios del sistema (en este caso, no filtramos por trainerId, ya que todos los ejercicios son accesibles)
        List<Exercise> exercises = exerciseService.findAllExercisesByTrainerId(trainerId);
        exerciseData.addAll(exercises);
        exerciseTable.setItems(exerciseData);
    }

    // Crear un nuevo ejercicio
    @FXML
    public void handleCreateExercise() throws DatabaseOperationException {
        try {
            URL url = getClass().getResource("/create_exercise_view.fxml");
            System.out.println("Fran. url: " + url);
            FXMLLoader loader = new FXMLLoader(url);

            // Obtener el controlador de la vista de creación de ejercicio
            ExerciseController controller = new ExerciseController(trainer);

            loader.setController(controller);

            Parent root = loader.load();
            // Configurar la escena y mostrar la nueva ventana
            Stage stage = new Stage();
            stage.setTitle("Crear Ejercicio");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de creación de ejercicio.");
        }
    }

    // Editar ejercicio existente
    @FXML
    public void handleEditExercise() throws DatabaseOperationException {
        Exercise selectedExercise = exerciseTable.getSelectionModel().getSelectedItem();
        if (selectedExercise != null) {
            Exercise updatedExercise = showExerciseDialog(selectedExercise);
            if (updatedExercise != null) {
                exerciseService.updateExercise(updatedExercise);
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
                exerciseService.deleteExercise(selectedExercise);
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