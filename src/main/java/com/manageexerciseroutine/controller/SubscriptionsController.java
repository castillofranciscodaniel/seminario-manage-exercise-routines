package com.manageexerciseroutine.controller;

import com.manageexerciseroutine.model.Subscription;
import com.manageexerciseroutine.service.SubscriptionService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
    private final int userId; // Suponiendo que tienes el ID del usuario logueado

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
    }

    // Formatear fechas a String
    private SimpleStringProperty formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return new SimpleStringProperty(dateFormat.format(date));
    }
}
