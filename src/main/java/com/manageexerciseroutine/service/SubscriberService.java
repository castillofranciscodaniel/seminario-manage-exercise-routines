package com.manageexerciseroutine.service;

import com.manageexerciseroutine.exeptions.DatabaseOperationException;
import com.manageexerciseroutine.model.Subscriber;
import com.manageexerciseroutine.repository.SubscriberRepositoryImpl;

import java.util.List;

public class SubscriberService {

    private final SubscriberRepositoryImpl subscriberRepository;

    public SubscriberService() {
        this.subscriberRepository = new SubscriberRepositoryImpl();
    }

    public SubscriberService(SubscriberRepositoryImpl subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    public void registerSubscriber(Subscriber subscriber) throws DatabaseOperationException {
        subscriberRepository.save(subscriber);
    }

    public List<Subscriber> getAllSubscribers() throws DatabaseOperationException {
        return subscriberRepository.findAll();
    }

    public Subscriber getSubscriberById(int id) throws DatabaseOperationException {
        return subscriberRepository.findById(id);
    }

    public void updateSubscriber(Subscriber subscriber) throws DatabaseOperationException {
        subscriberRepository.update(subscriber);
    }

    public void deleteSubscriber(int id) throws DatabaseOperationException {
        subscriberRepository.delete(id);
    }

    public List<Subscriber> getSubscribersByRegistrationDate(String date) throws DatabaseOperationException {
        return subscriberRepository.findByRegistrationDate(date);
    }

    public Subscriber loginSubscriber(String email, String password) throws DatabaseOperationException {
        return subscriberRepository.findByEmailAndPassword(email, password);
    }
}
