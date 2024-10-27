package com.manageexerciseroutine;

import com.manageexerciseroutine.controller.LoginController;
import com.manageexerciseroutine.repository.TrainerRepositoryImpl;
import com.manageexerciseroutine.repository.SubscriberRepositoryImpl;
import com.manageexerciseroutine.service.TrainerService;
import com.manageexerciseroutine.service.SubscriberService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private TrainerService trainerService;
    private SubscriberService subscriberService;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Inicializar repositorios y servicios
        TrainerRepositoryImpl trainerRepository = new TrainerRepositoryImpl();
        SubscriberRepositoryImpl subscriberRepository = new SubscriberRepositoryImpl();

        trainerService = new TrainerService(trainerRepository);
        subscriberService = new SubscriberService(subscriberRepository);

        // Cargar el controlador de login
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login_view.fxml"));
        LoginController loginController = new LoginController(trainerService, subscriberService);
        loader.setController(loginController);

        Parent root = loader.load();
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
