package com.manageexerciseroutine.controller;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.Routine;
import com.manageexerciseroutine.model.Trainer;
import com.manageexerciseroutine.service.RoutineService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class RoutineABMController {

    private final RoutineService routineService = new RoutineService();
    private final int trainerId;
    private final ObservableList<Routine> routineData = FXCollections.observableArrayList();

    @FXML
    private TableView<Routine> routineTable;

    @FXML
    private TableColumn<Routine, String> nameColumn;

    @FXML
    private TableColumn<Routine, String> descriptionColumn;

    @FXML
    private TableColumn<Routine, Integer> durationColumn;

    public RoutineABMController(int trainerId) {
        this.trainerId = trainerId;
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        durationColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDuration()).asObject());

        loadRoutines();
    }

    private void loadRoutines() {
        try {
            List<Routine> routines = routineService.findRoutinesByTrainerId(trainerId);
            routineData.setAll(routines);
            routineTable.setItems(routineData);
        } catch (DatabaseOperationException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar las rutinas.");
        }
    }
    @FXML
    private void handleCreateRoutine() {
        openRoutineDialog(null);
    }

    @FXML
    private void handleEditRoutine() {
        Routine selectedRoutine = routineTable.getSelectionModel().getSelectedItem();
        if (selectedRoutine != null) {
            openRoutineDialog(selectedRoutine);
        } else {
            showAlert(Alert.AlertType.WARNING, "Selección Vacía", "Por favor selecciona una rutina para editar.");
        }
    }

    private void openRoutineDialog(Routine routineToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/create_routine_view.fxml"));
            RoutineController controller = routineToEdit == null ? new RoutineController(trainerId) : new RoutineController(trainerId, routineToEdit);
            loader.setController(controller);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(routineToEdit == null ? "Crear Rutina" : "Editar Rutina");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadRoutines();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de rutina.");
        }
    }

    @FXML
    private void handleDeleteRoutine() {
        Routine selectedRoutine = routineTable.getSelectionModel().getSelectedItem();
        if (selectedRoutine != null) {
            Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION, "Confirmar Eliminación", "¿Estás seguro de que deseas eliminar esta rutina?");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    routineService.deleteRoutine(selectedRoutine);
                    routineData.remove(selectedRoutine);
                } catch (DatabaseOperationException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "No se pudo eliminar la rutina.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selección Vacía", "Por favor selecciona una rutina para eliminar.");
        }
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
