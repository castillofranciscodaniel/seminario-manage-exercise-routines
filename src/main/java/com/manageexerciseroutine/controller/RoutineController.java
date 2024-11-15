package com.manageexerciseroutine.controller;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.ConfiguredExercise;
import com.manageexerciseroutine.model.Routine;
import com.manageexerciseroutine.model.Trainer;
import com.manageexerciseroutine.service.ConfiguredExerciseService;
import com.manageexerciseroutine.service.RoutineService;
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
import lombok.Getter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RoutineController {

    private final RoutineService routineService = new RoutineService();
    private final TrainerService trainerService = new TrainerService();
    private final ConfiguredExerciseService configuredExerciseService = new ConfiguredExerciseService();

    private Trainer trainer;  // Cambiado para cargar el objeto completo desde el ID

    @Getter
    private Routine routine;
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
        System.out.println("trainerId: " + trainerId + ", routineToEdit: " + routineToEdit);
        this.routine = routineToEdit == null ? new Routine() : routineToEdit;
    }

    @FXML
    public void initialize() {
        // Poblar los ComboBox con los valores correspondientes
        difficultyLevelComboBox.setItems(FXCollections.observableArrayList(
                Arrays.stream(Routine.DifficultyLevel.values()).map(Enum::name).toList()  // Enum de nivel de dificultad
        ));
        trainingTypeComboBox.setItems(FXCollections.observableArrayList(
                Arrays.stream(Routine.TrainingType.values()).map(Enum::name).toList()  // Enum de tipo de entrenamiento
        ));

        if (routine != null) {
            nameField.setText(routine.getName());
            descriptionField.setText(routine.getDescription());
            durationField.setText(String.valueOf(routine.getDuration()));

            // Asignar valores a los ComboBox solo si no son null
            if (routine.getDifficultyLevel() != null) {
                difficultyLevelComboBox.setValue(routine.getDifficultyLevel().name());
            }
            if (routine.getTrainingType() != null) {
                trainingTypeComboBox.setValue(routine.getTrainingType().name());
            }

            // mostrar los ejercicios configurados de la rutina
            // Configura las columnas de la tabla
            exerciseNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExercise().getName()));
            repetitionsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRepetitions()).asObject());
            seriesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSeries()).asObject());
            restColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRest()).asObject());

            // Configura la tabla para usar la lista ObservableList
            configuredExercisesTable.setItems(exercises);

            // Cargar los ejercicios configurados solo si la rutina ya existe
            if (routine != null && routine.getId() != 0) {
                loadConfiguredExercises();
            }
        }
    }

    private void loadConfiguredExercises() {
        try {
            List<ConfiguredExercise> loadedExercises = configuredExerciseService.findExercisesByRoutineId(routine.getId());
            exercises.setAll(loadedExercises);  // Cargar los ejercicios en la lista Observable
        } catch (DatabaseOperationException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los ejercicios configurados: " + e.getMessage());
        }
    }

    @FXML
    public void handleSaveRoutine() {
        try {
            String name = nameField.getText();
            routine.setName(name);
            routine.setTrainer(trainer);
            routine.setDescription(descriptionField.getText());
            routine.setDuration(Integer.parseInt(durationField.getText()));
            routine.setDifficultyLevel(Routine.DifficultyLevel.valueOf(difficultyLevelComboBox.getValue()));
            routine.setTrainingType(Routine.TrainingType.valueOf(trainingTypeComboBox.getValue()));


            if (routine.getId() == 0) {
                // Crear nueva rutina
                routine = routineService.saveRoutineWithExercises(routine, exercises);
            } else {
                // Editar rutina existente
                routineService.updateRoutineWithExercises(routine, exercises);
            }
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "¡Rutina guardada exitosamente!");

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();

        } catch (DatabaseOperationException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo guardar la rutina: " + e.getMessage());
        }
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);  // Deja el encabezado vacío si no es necesario
        alert.setContentText(message);

        ButtonType buttonTypeOk = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);  // Configura el botón "Aceptar"
        alert.getButtonTypes().setAll(buttonTypeOk);  // Añade el botón al cuadro de diálogo

        alert.showAndWait();
    }

    @FXML
    public void handleAddExercise() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/configured_exercise_form.fxml"));
            ConfiguredExerciseController controller = new ConfiguredExerciseController(trainer.getId(), routine);
            loader.setController(controller);

            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar Ejercicio Configurado");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Después de que el usuario guarda el ejercicio, lo añadimos a la lista
            ConfiguredExercise newExercise = controller.getConfiguredExercise();
            if (newExercise != null) {
                exercises.add(newExercise);  // `exercises` es la lista observable vinculada a la tabla
                configuredExercisesTable.refresh();  // Refrescar la tabla para mostrar el nuevo ejercicio
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




