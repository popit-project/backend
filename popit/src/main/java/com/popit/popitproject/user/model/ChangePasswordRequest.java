package com.popit.popitproject.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    private String userId;
    private String oldPassword;
    private String newPassword;

    public ChangePasswordRequest() {
    }

    public ChangePasswordRequest(String userId, String oldPassword, String newPassword) {
        this.userId = userId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}