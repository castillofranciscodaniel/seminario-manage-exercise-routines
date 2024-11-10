package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.Subscription;

import java.util.List;

public interface SubscriptionRepository {

    List<Subscription> findActiveSubscriptionsByUserId(int userId) throws DatabaseOperationException;

    void markSubscriptionAsEnded(int subscriptionId) throws DatabaseOperationException;

    void save(Subscription subscription) throws DatabaseOperationException;

    void update(Subscription subscription) throws DatabaseOperationException;
}
