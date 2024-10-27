package com.manageexerciseroutine.service;

import com.manageexerciseroutine.model.Subscriber;
import com.manageexerciseroutine.repository.SubscriberRepositoryImpl;

import java.sql.SQLException;
import java.util.List;

public class SubscriberService {

    private final SubscriberRepositoryImpl subscriberRepository;

    public SubscriberService(SubscriberRepositoryImpl subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    public void registerSubscriber(Subscriber subscriber) throws SQLException {
        subscriberRepository.save(subscriber);
    }

    public List<Subscriber> getAllSubscribers() throws SQLException {
        return subscriberRepository.findAll();
    }

    public Subscriber getSubscriberById(int id) throws SQLException {
        return subscriberRepository.findById(id);
    }

    public void updateSubscriber(Subscriber subscriber) throws SQLException {
        subscriberRepository.update(subscriber);
    }

    public void deleteSubscriber(int id) throws SQLException {
        subscriberRepository.delete(id);
    }

    public List<Subscriber> getSubscribersByRegistrationDate(String date) throws SQLException {
        return subscriberRepository.findByRegistrationDate(date);
    }

    public Subscriber loginSubscriber(String email, String password) throws SQLException {
        return subscriberRepository.findByEmailAndPassword(email, password);
    }
}
