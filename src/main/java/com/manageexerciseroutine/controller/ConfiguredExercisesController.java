package com.manageexerciseroutine.controller;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.ConfiguredExercise;
import com.manageexerciseroutine.model.Subscription;
import com.manageexerciseroutine.service.ConfiguredExerciseService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class ConfiguredExercisesController {

    @FXML
    private TableView<ConfiguredExercise> exerciseTable;

    @FXML
    private TableColumn<ConfiguredExercise, String> exerciseNameColumn;

    @FXML
    private TableColumn<ConfiguredExercise, Integer> orderColumn;

    @FXML
    private TableColumn<ConfiguredExercise, Integer> repetitionsColumn;

    @FXML
    private TableColumn<ConfiguredExercise, Integer> seriesColumn;

    private final ObservableList<ConfiguredExercise> exerciseData = FXCollections.observableArrayList();
    private final ConfiguredExerciseService configuredExerciseService;
    private final Subscription subscription;

    // Constructor con inyección del servicio y la suscripción
    public ConfiguredExercisesController(ConfiguredExerciseService configuredExerciseService, Subscription subscription) {
        this.configuredExerciseService = configuredExerciseService;
        this.subscription = subscription;
    }

    @FXML
    public void initialize() throws DatabaseOperationException {
        // Inicializar las columnas de la tabla
        exerciseNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExercise().getName()));
        orderColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getOrderIndex()).asObject());
        repetitionsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRepetitions()).asObject());
        seriesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSeries()).asObject());

        // Cargar los ejercicios configurados para la suscripción seleccionada
        List<ConfiguredExercise> exercises = configuredExerciseService.findExercisesByRoutineId(subscription.getRoutine().getId());
        exerciseData.addAll(exercises);
        exerciseTable.setItems(exerciseData);
    }
}
