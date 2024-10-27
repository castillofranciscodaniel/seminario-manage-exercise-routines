package com.manageexerciseroutine.controller;

import com.manageexerciseroutine.model.Routine;
import com.manageexerciseroutine.service.RoutineService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RoutineController {

    @FXML
    private TableView<Routine> routineTable;

    @FXML
    private TableColumn<Routine, String> nameColumn;

    @FXML
    private TableColumn<Routine, String> descriptionColumn;

    @FXML
    private TableColumn<Routine, Integer> durationColumn;

    private final RoutineService routineService;
    private final ObservableList<Routine> routineData = FXCollections.observableArrayList();

    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @FXML
    public void initialize() throws SQLException {
        // Configurar la tabla para mostrar los datos de las rutinas
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        // Usar SimpleIntegerProperty para valores numéricos
        durationColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDuration()).asObject());

        // Cargar las rutinas desde el servicio
        List<Routine> routines = routineService.findAllRoutines();
        routineData.addAll(routines);
        routineTable.setItems(routineData);
    }

    @FXML
    public void handleCreateRoutine() throws SQLException {
        Routine newRoutine = showRoutineDialog(new Routine());
        if (newRoutine != null) {
            routineService.saveRoutine(newRoutine); // Guardar en la base de datos
            routineData.add(newRoutine); // Actualizar la tabla
        }
    }

    @FXML
    public void handleEditRoutine() throws SQLException {
        Routine selectedRoutine = routineTable.getSelectionModel().getSelectedItem();
        if (selectedRoutine != null) {
            Routine updatedRoutine = showRoutineDialog(selectedRoutine);
            if (updatedRoutine != null) {
                routineService.updateRoutine(updatedRoutine); // Actualizar en la base de datos
                routineTable.refresh(); // Refrescar la tabla
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a routine to edit.");
        }
    }

    @FXML
    public void handleDeleteRoutine() throws SQLException {
        Routine selectedRoutine = routineTable.getSelectionModel().getSelectedItem();
        if (selectedRoutine != null) {
            Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION, "Confirm Delete", "Are you sure you want to delete this routine?");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                routineService.deleteRoutine(selectedRoutine); // Eliminar de la base de datos
                routineData.remove(selectedRoutine); // Remover de la tabla
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a routine to delete.");
        }
    }

    private Routine showRoutineDialog(Routine routine) {
        // Este código es muy básico; idealmente tendrías una ventana de diálogo más completa para editar la rutina
        TextInputDialog dialog = new TextInputDialog(routine.getName());
        dialog.setTitle("Routine Dialog");
        dialog.setHeaderText("Enter routine details:");
        dialog.setContentText("Routine Name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            routine.setName(result.get());
            routine.setDescription("Sample description");  // Este valor debería ser ajustable en un formulario más completo
            routine.setDuration(60);  // Duración de ejemplo, debe ser ajustable
            return routine;
        }
        return null;
    }

    private Optional<ButtonType> showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait();
    }
}
