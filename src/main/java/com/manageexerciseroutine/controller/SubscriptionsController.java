package com.manageexerciseroutine.controller;

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
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    public void initialize() throws SQLException {
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
        stage.setTitle("Configured Exercises");
        stage.setScene(new javafx.scene.Scene(root, 600, 400));
        stage.show();
    }


    // Formatear fechas a String
    private SimpleStringProperty formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return new SimpleStringProperty(dateFormat.format(date));
    }
}
