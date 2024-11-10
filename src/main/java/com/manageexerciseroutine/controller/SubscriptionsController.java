package com.manageexerciseroutine.controller;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.Subscription;
import com.manageexerciseroutine.repository.ConfiguredExerciseRepository;
import com.manageexerciseroutine.repository.ConfiguredExerciseRepositoryImpl;
import com.manageexerciseroutine.service.ConfiguredExerciseService;
import com.manageexerciseroutine.service.SubscriptionService;
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
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class SubscriptionsController {

    @FXML
    private TableView<Subscription> subscriptionTable;

    @FXML
    private TableColumn<Subscription, String> routineNameColumn;

    @FXML
    private TableColumn<Subscription, Integer> durationColumn;

    private final ObservableList<Subscription> subscriptionData = FXCollections.observableArrayList();
    private final SubscriptionService subscriptionService;
    private final int userId; // ID del usuario logueado

    public SubscriptionsController(SubscriptionService subscriptionService, int userId) {
        this.subscriptionService = subscriptionService;
        this.userId = userId;
    }

    @FXML
    public void initialize() throws DatabaseOperationException, SQLException {
        // Inicializar columnas
        routineNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoutine().getName()));
        durationColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRoutine().getDuration()).asObject());

        // Buscar suscripciones del usuario logueado
        List<Subscription> subscriptions = subscriptionService.findSubscriptionsByUserId(userId);
        subscriptionData.addAll(subscriptions);
        subscriptionTable.setItems(subscriptionData);

        // Añadir listener para seleccionar una suscripción
        subscriptionTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedSubscription) -> {
            if (selectedSubscription != null) {
                try {
                    showConfiguredExercises(selectedSubscription);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showConfiguredExercises(Subscription subscription) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/configured_exercises_view.fxml"));

        // Crear el servicio ConfiguredExerciseService
        ConfiguredExerciseRepository configuredExerciseRepository = new ConfiguredExerciseRepositoryImpl();
        ConfiguredExerciseService configuredExerciseService = new ConfiguredExerciseService(configuredExerciseRepository);

        // Crear el controlador manualmente y pasarle el servicio y la suscripción seleccionada
        ConfiguredExercisesController controller = new ConfiguredExercisesController(configuredExerciseService, subscription);
        loader.setController(controller);

        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Ejercicios configurados");
        stage.setScene(new javafx.scene.Scene(root, 600, 400));
        stage.show();
    }


    // Formatear fechas a String
    private SimpleStringProperty formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return new SimpleStringProperty(dateFormat.format(date));
    }

    // Método para suscribirse a nuevas rutinas
    @FXML
    public void handleSubscribeToNewRoutine() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/choose_routine_view.fxml"));
            Parent root = loader.load();

            // Pasar el controlador con userId
            ChooseRoutineController controller = loader.getController();
            controller.setUserId(userId);
            controller.setSubscriptionService(subscriptionService);

            Stage stage = new Stage();
            stage.setTitle("Elegir Nueva Rutina");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Actualizar la tabla después de la suscripción
            subscriptionData.clear();
            subscriptionData.addAll(subscriptionService.findSubscriptionsByUserId(userId));

        } catch (IOException | DatabaseOperationException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana para elegir nuevas rutinas.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Método para eliminar una suscripción
    @FXML
    public void handleDeleteSubscription() throws DatabaseOperationException, SQLException {
        Subscription selectedSubscription = subscriptionTable.getSelectionModel().getSelectedItem();
        if (selectedSubscription != null) {
            Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION, "Confirmar Eliminación", "¿Deseas eliminar esta suscripción?");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                subscriptionService.deleteSubscription(selectedSubscription);
                subscriptionData.remove(selectedSubscription); // Actualizar la tabla
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selección vacía", "Selecciona una suscripción para eliminar.");
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
