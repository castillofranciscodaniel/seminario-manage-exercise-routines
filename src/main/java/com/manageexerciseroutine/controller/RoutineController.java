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
import javafx.scene.control.*;
import javafx.stage.Stage;

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
        routine.setDifficultyLevel(difficultyLevel);
        routine.setTrainingType(trainingType);
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
        // Lógica para agregar un ejercicio configurado a la lista
        ConfiguredExercise newExercise = new ConfiguredExercise();  // Crea una nueva instancia con valores de ejemplo o seleccionados
        exercises.add(newExercise);
    }

    @FXML
    public void handleRemoveExercise() {
        ConfiguredExercise selectedExercise = configuredExercisesTable.getSelectionModel().getSelectedItem();
        if (selectedExercise != null) {
            exercises.remove(selectedExercise);
        }
    }

}




