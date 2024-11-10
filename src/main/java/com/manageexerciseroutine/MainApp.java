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

        // Crear el FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/login_view.fxml"));

        // Inyectar el controlador con dependencias antes de cargar la vista
        LoginController loginController = new LoginController(trainerService, subscriberService);
        loader.setController(loginController);  // Especificar el controlador aquí

        // Cargar la vista
        Parent root = loader.load();  // Cargar la vista FXML después de especificar el controlador

        // Configurar la escena
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.setMaximized(true); // Maximizar la ventana
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
