package com.manageexerciseroutine.service;

import com.manageexerciseroutine.model.Routine;
import com.manageexerciseroutine.model.Subscriber;
import com.manageexerciseroutine.model.Subscription;
import com.manageexerciseroutine.repository.SubscriptionRepository;
import com.manageexerciseroutine.repository.SubscriptionRepositoryImpl;

import java.sql.SQLException;
import java.util.List;

public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public SubscriptionService() {
        this.subscriptionRepository = new SubscriptionRepositoryImpl();
    }

    public List<Subscription> findActiveSubscriptionsByUserId(int userId) throws SQLException {
        return subscriptionRepository.findActiveSubscriptionsByUserId(userId);
    }

    public void subscribeToRoutine(int userId, Routine routine) throws SQLException {
        // Crear una nueva suscripción con la información del usuario y la rutina
        Subscription newSubscription = new Subscription();
        newSubscription.setSubscriber(new Subscriber(userId));
        newSubscription.setRoutine(routine);
        newSubscription.setStartDate(new java.util.Date()); // Fecha de inicio actual
        newSubscription.setStatus(Subscription.Status.ACTIVE);

        // Guardar la nueva suscripción en la base de datos
        subscriptionRepository.save(newSubscription);
    }

    // SubscriptionService.java
    public void endSubscription(int subscriptionId) throws SQLException {
        subscriptionRepository.markSubscriptionAsEnded(subscriptionId);
    }
}
