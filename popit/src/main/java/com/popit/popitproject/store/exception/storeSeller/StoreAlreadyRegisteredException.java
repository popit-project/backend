package com.popit.popitproject.store.exception.storeSeller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class StoreAlreadyRegisteredException extends RuntimeException {

    public StoreAlreadyRegisteredException(String message) {
        super(message);
    }

}