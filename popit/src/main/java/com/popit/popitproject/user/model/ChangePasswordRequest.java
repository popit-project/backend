package com.popit.popitproject.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    private String userId;
    private String oldPassword;
    private String newPassword;
}