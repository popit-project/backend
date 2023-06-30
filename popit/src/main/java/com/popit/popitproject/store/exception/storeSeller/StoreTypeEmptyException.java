package com.popit.popitproject.store.exception.storeSeller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StoreTypeEmptyException extends RuntimeException {

    public StoreTypeEmptyException(String message) {
        super(message);
    }
}