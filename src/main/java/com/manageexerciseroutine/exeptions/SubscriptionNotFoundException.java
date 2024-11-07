package com.manageexerciseroutine.exeptions;

public class SubscriptionNotFoundException extends RuntimeException {
    public SubscriptionNotFoundException(int id) {
        super("Subscription with ID " + id + " not found.");
    }

    public SubscriptionNotFoundException(String message) {
        super(message);
    }
}
