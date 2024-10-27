package com.manageexerciseroutine.controller;

import com.manageexerciseroutine.model.Subscription;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class SubscriptionsController {

    @FXML
    private TableView<Subscription> subscriptionTable;

    @FXML
    private TableColumn<Subscription, String> routineNameColumn;

    @FXML
    private TableColumn<Subscription, String> startDateColumn;

    @FXML
    private TableColumn<Subscription, String> endDateColumn;

    private final ObservableList<Subscription> subscriptionData = FXCollections.observableArrayList();
    private final List<Subscription> subscriptions;

    public SubscriptionsController(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @FXML
    public void initialize() {
        routineNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoutine().getName()));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        subscriptionData.addAll(subscriptions);
        subscriptionTable.setItems(subscriptionData);
    }

    @FXML
    public void handleCancelSubscription() {
        Subscription selectedSubscription = subscriptionTable.getSelectionModel().getSelectedItem();
        if (selectedSubscription != null) {
            Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION, "Confirm Cancellation", "Are you sure you want to cancel this subscription?");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                subscriptionData.remove(selectedSubscription);
                subscriptions.remove(selectedSubscription);
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a subscription to cancel.");
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
