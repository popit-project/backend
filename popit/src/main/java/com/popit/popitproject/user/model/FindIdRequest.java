package com.popit.popitproject.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindIdRequest {
    private String email;

    public FindIdRequest() {
    }

    public FindIdRequest(String email) {
        this.email = email;
    }
}