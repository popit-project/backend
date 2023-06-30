package com.popit.popitproject.store.exception.storeSeller;

public class StoreAlreadyRegisteredException extends RuntimeException {
    public StoreAlreadyRegisteredException(String message) {
        super(message);
    }
}