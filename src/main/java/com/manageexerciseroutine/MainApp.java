package com.manageexerciseroutine;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlURL = getClass().getResource("/exercise_view.fxml");
        System.out.println("Fran:" + fxmlURL);

        Parent root = FXMLLoader.load(getClass().getResource("/exercise_view.fxml"));
        primaryStage.setTitle("Exercise Management");
        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
