package com.manageexerciseroutine.controller;

import com.manageexerciseroutine.service.TrainerService;
import com.manageexerciseroutine.service.SubscriberService;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class MainMenuController {

    public MainMenuController(TrainerService trainerService, SubscriberService subscriberService) {
    }

    @FXML
    public void handleRegisterTrainer() {
        // Lógica para registrar un Trainer
        System.out.println("Register Trainer - Use TrainerService");
    }

    @FXML
    public void handleRegisterSubscriber() {
        // Lógica para registrar un Subscriber
        System.out.println("Register Subscriber - Use SubscriberService");
    }

    @FXML
    public void handleLogin() {
        // Lógica de login
        System.out.println("Login logic here...");
    }
}
