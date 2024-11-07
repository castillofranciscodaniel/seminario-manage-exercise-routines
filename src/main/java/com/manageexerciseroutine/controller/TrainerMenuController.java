package com.manageexerciseroutine.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class TrainerMenuController {

    private final int trainerId;

    // Constructor que recibe el trainerId
    public TrainerMenuController(int trainerId) {
        this.trainerId = trainerId;
    }

    @FXML
    public void handleManageExercises() throws Exception {
        // Redirigir al ABM de Ejercicios pasando el trainerId
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/exercise_abm_view.fxml"));

        // Crear el controlador de Exercise ABM pasando el trainerId
        ExerciseABMController controller = new ExerciseABMController(trainerId);
        loader.setController(controller);

        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Gestión del ejercicio");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    @FXML
    public void handleManageRoutines() throws Exception {
        // Redirigir al ABM de Rutinas pasando el trainerId
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/routine_abm_view.fxml"));

        // Crear el controlador de Routine ABM pasando el trainerId
        RoutineABMController controller = new RoutineABMController(trainerId);
        loader.setController(controller);

        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Gestión de rutina");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }
}
