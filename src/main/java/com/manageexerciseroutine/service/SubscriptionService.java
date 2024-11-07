package com.manageexerciseroutine.service;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.Subscription;
import com.manageexerciseroutine.repository.SubscriptionRepository;

import java.sql.SQLException;
import java.util.List;

public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    // Método para encontrar suscripciones por ID de usuario
    public List<Subscription> findSubscriptionsByUserId(int userId) throws DatabaseOperationException {
        return subscriptionRepository.findBySubscriberId(userId);
    }

    // Método para guardar una nueva suscripción
    public void saveSubscription(Subscription subscription) throws DatabaseOperationException {
        subscriptionRepository.save(subscription);
    }

    // Método para eliminar una suscripción
    public void deleteSubscription(Subscription subscription) throws DatabaseOperationException {
        subscriptionRepository.delete(subscription);
    }
}
