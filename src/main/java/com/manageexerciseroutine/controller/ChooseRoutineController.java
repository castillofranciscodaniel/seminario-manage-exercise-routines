package com.manageexerciseroutine.controller;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.Routine;
import com.manageexerciseroutine.service.RoutineService;
import com.manageexerciseroutine.service.SubscriptionService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ChooseRoutineController {

    @FXML
    private TableView<Routine> routineTable;
    @FXML
    private TableColumn<Routine, String> nameColumn;
    @FXML
    private TableColumn<Routine, String> descriptionColumn;
    @FXML
    private TableColumn<Routine, Integer> durationColumn;

    private final ObservableList<Routine> routineData = FXCollections.observableArrayList();
    private SubscriptionService subscriptionService;
    private int userId; // ID del usuario actual

    private final RoutineService routineService = new RoutineService();

    // Establecer el ID del usuario logueado
    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Establecer el servicio de suscripciones
    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @FXML
    public void initialize() {
        // Configuración de las columnas de la tabla
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        durationColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDuration()).asObject());

        // Cargar las rutinas disponibles
        loadRoutines();
    }

    private void loadRoutines() {
        try {
            List<Routine> routines = routineService.findAllRoutines();
            routineData.addAll(routines);
            routineTable.setItems(routineData);
        } catch (DatabaseOperationException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudieron cargar las rutinas: " + e.getMessage());
        }
    }

    @FXML
    public void handleSubscribe() {
        Routine selectedRoutine = routineTable.getSelectionModel().getSelectedItem();
        if (selectedRoutine != null) {
            // Confirmación de suscripción
            Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION, "Confirmar Suscripción", "¿Deseas suscribirte a esta rutina?");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    // Realizar la suscripción
                    subscriptionService.subscribeToRoutine(userId, selectedRoutine);
                    showAlert(Alert.AlertType.INFORMATION, "Subscripción exitosa", "Te has suscrito a la rutina seleccionada.");
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "No se pudo completar la suscripción: " + e.getMessage());
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selección vacía", "Por favor selecciona una rutina para suscribirte.");
        }
    }

    private Optional<ButtonType> showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait();
    }
}
