package com.manageexerciseroutine.repository;

import com.manageexerciseroutine.model.Subscription;

import java.sql.SQLException;
import java.util.List;

public interface SubscriptionRepository {
    List<Subscription> findBySubscriberId(int subscriberId) throws SQLException;

    void save(Subscription subscription) throws SQLException;

    void delete(Subscription subscription) throws SQLException;

    void update(Subscription subscription) throws SQLException;
}
