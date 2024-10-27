package com.manageexerciseroutine.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import com.manageexerciseroutine.repository.ExerciseRepository;
import com.manageexerciseroutine.repository.ExerciseRepositoryImpl;
import com.manageexerciseroutine.service.ExerciseService;

public class ExerciseController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField durationField;
    @FXML
    private TextField typeField;
    @FXML
    private ListView<String> exerciseList;

    private ExerciseService exerciseService;

    public ExerciseController() {
        ExerciseRepository exerciseRepository = new ExerciseRepositoryImpl();
        this.exerciseService = new ExerciseService(exerciseRepository);
    }

    @FXML
    public void initialize() {
        updateExerciseList();
    }

    @FXML
    public void addExercise() {
        String name = nameField.getText();
        String description = descriptionField.getText();
        int duration = Integer.parseInt(durationField.getText());
        String type = typeField.getText();

        exerciseService.addExercise(name, description, duration, type);
        updateExerciseList();
    }

    @FXML
    public void deleteExercise() {
        String selectedExercise = exerciseList.getSelectionModel().getSelectedItem();
        exerciseService.deleteExercise(selectedExercise);
        updateExerciseList();
    }

    private void updateExerciseList() {
        exerciseList.getItems().clear();
        exerciseService.getExercises().forEach(ex -> exerciseList.getItems().add(ex.getName()));
    }
}
