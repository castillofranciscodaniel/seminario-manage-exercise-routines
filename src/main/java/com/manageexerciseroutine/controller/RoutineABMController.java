package com.manageexerciseroutine.controller;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.Routine;
import com.manageexerciseroutine.model.Trainer;
import com.manageexerciseroutine.repository.RoutineRepository;
import com.manageexerciseroutine.repository.RoutineRepositoryImpl;
import com.manageexerciseroutine.service.RoutineService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Optional;

public class RoutineABMController {

    @FXML
    private TableView<Routine> routineTable;

    @FXML
    private TableColumn<Routine, String> nameColumn;

    @FXML
    private TableColumn<Routine, String> descriptionColumn;

    @FXML
    private TableColumn<Routine, Integer> durationColumn;

    private final ObservableList<Routine> routineData = FXCollections.observableArrayList();
    private final RoutineService routineService;
    private final int trainerId;

    // Constructor con inyección de servicios y trainerId
    public RoutineABMController(int trainerId) {
        this.trainerId = trainerId;
        RoutineRepository routineRepository = new RoutineRepositoryImpl();
        this.routineService = new RoutineService(routineRepository);  // Instancia del servicio de rutinas
    }

    @FXML
    public void initialize() throws DatabaseOperationException {
        // Inicializar columnas de la tabla
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        durationColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDuration()).asObject());

        // Cargar rutinas del entrenador logueado (trainerId)
        List<Routine> routines = routineService.findRoutinesByTrainerId(trainerId);
        routineData.addAll(routines);
        routineTable.setItems(routineData);
    }

    // Crear una nueva rutina
    @FXML
    public void handleCreateRoutine() throws DatabaseOperationException {
        Routine newRoutine = showRoutineDialog(new Routine());
        if (newRoutine != null) {
            newRoutine.setTrainer(new Trainer(trainerId));  // Asignar el trainerId a la nueva rutina
            routineService.saveRoutine(newRoutine);
            routineData.add(newRoutine);
        }
    }

    // Editar rutina existente
    @FXML
    public void handleEditRoutine() throws DatabaseOperationException {
        Routine selectedRoutine = routineTable.getSelectionModel().getSelectedItem();
        if (selectedRoutine != null) {
            Routine updatedRoutine = showRoutineDialog(selectedRoutine);
            if (updatedRoutine != null) {
                routineService.updateRoutine(updatedRoutine);
                routineTable.refresh();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a routine to edit.");
        }
    }

    // Eliminar rutina seleccionada
    @FXML
    public void handleDeleteRoutine() throws DatabaseOperationException {
        Routine selectedRoutine = routineTable.getSelectionModel().getSelectedItem();
        if (selectedRoutine != null) {
            Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION, "Confirm Delete", "Are you sure you want to delete this routine?");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                routineService.deleteRoutine(selectedRoutine);
                routineData.remove(selectedRoutine);
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a routine to delete.");
        }
    }

    // Mostrar el diálogo para crear/editar una rutina
    private Routine showRoutineDialog(Routine routine) {
        TextInputDialog dialog = new TextInputDialog(routine.getName());
        dialog.setTitle("Routine Dialog");
        dialog.setHeaderText("Enter routine details:");
        dialog.setContentText("Routine Name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            routine.setName(result.get());
            routine.setDescription("Sample description");  // Ajustar descripción en un formulario más completo
            routine.setDuration(60);  // Duración por defecto
            return routine;
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
