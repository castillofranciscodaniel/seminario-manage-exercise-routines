package com.manageexerciseroutine.controller;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.ConfiguredExercise;
import com.manageexerciseroutine.model.Routine;
import com.manageexerciseroutine.model.Trainer;
import com.manageexerciseroutine.service.RoutineService;
import com.manageexerciseroutine.service.TrainerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class RoutineController {

    private final RoutineService routineService = new RoutineService();
    private final TrainerService trainerService = new TrainerService();
    private Trainer trainer;  // Cambiado para cargar el objeto completo desde el ID
    private Routine routineToEdit;
    private final ObservableList<ConfiguredExercise> exercises = FXCollections.observableArrayList();


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
    @FXML
    private TableColumn<ConfiguredExercise, String> exerciseNameColumn;
    @FXML
    private TableColumn<ConfiguredExercise, Integer> orderColumn;
    @FXML
    private TableColumn<ConfiguredExercise, Integer> repetitionsColumn;
    @FXML
    private TableColumn<ConfiguredExercise, Integer> seriesColumn;
    @FXML
    private TableColumn<ConfiguredExercise, Integer> restColumn;

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
        difficultyLevelComboBox.setItems(FXCollections.observableArrayList(
                Arrays.stream(Routine.DifficultyLevel.values()).map(Enum::name).toList())
        );

        trainingTypeComboBox.setItems(FXCollections.observableArrayList(
                        Arrays.stream(Routine.TrainingType.values()).map(Enum::name).toList()
                )
        );

        if (routineToEdit != null) {
            nameField.setText(routineToEdit.getName());
            descriptionField.setText(routineToEdit.getDescription());
            durationField.setText(String.valueOf(routineToEdit.getDuration()));
            difficultyLevelComboBox.setValue(routineToEdit.getDifficultyLevel().name());
            trainingTypeComboBox.setValue(routineToEdit.getTrainingType().name());
        }
    }

    @FXML
    private void handleSaveRoutine() {
        // Implementación para guardar la rutina (creación o actualización)
        String name = nameField.getText();
        String description = descriptionField.getText();
        int duration = Integer.parseInt(durationField.getText());
        String difficultyLevel = difficultyLevelComboBox.getValue();
        String trainingType = trainingTypeComboBox.getValue();

        Routine routine = routineToEdit == null ? new Routine() : routineToEdit;
        routine.setName(name);
        routine.setDescription(description);
        routine.setDuration(duration);
        routine.setDifficultyLevel(Routine.DifficultyLevel.valueOf(difficultyLevel));
        routine.setTrainingType(Routine.TrainingType.valueOf(trainingType));
        routine.setTrainer(trainer);

        // Guardar la rutina en la base de datos y cerrar la ventana
        // (La implementación de guardado debe realizarse aquí o en un servicio)
        closeWindow();
    }

    @FXML
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

    @FXML
    public void handleAddExercise() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/configured_exercise_form.fxml"));
            ConfiguredExerciseController controller = new ConfiguredExerciseController(trainer.getId(), routineToEdit);
            loader.setController(controller);

            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar Ejercicio Configurado");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Después de que el usuario guarda el ejercicio, lo añadimos a la lista
            ConfiguredExercise newExercise = controller.getConfiguredExercise();
            if (newExercise != null) {
                exercises.add(newExercise);
                configuredExercisesTable.refresh();
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de configuración de ejercicios.");
        }
    }


    @FXML
    public void handleRemoveExercise() {
        ConfiguredExercise selectedExercise = configuredExercisesTable.getSelectionModel().getSelectedItem();
        if (selectedExercise != null) {
            exercises.remove(selectedExercise);
        }
    }

}




