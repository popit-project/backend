package com.popit.popitproject.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StatusCode {

    SELLER_STORE_CREATE(HttpStatus.CREATED, "Store is created."),
    SELLER_STORE_UPDATE(HttpStatus.OK, "Store info is updated.")
    ;

    private HttpStatus status;
    private String message;
}
