package com.popit.popitproject.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationResultDTO {
    private String userId;
    private String password;
    private String passwordCheck;
    private String email;
    private String phone;
    private String message;

    public RegistrationResultDTO(UserDTO userDto, String message) {
        this.userId = userDto.getUserId();
        this.password = userDto.getPassword();
        this.passwordCheck = userDto.getPasswordCheck();
        this.email = userDto.getEmail();
        this.phone = userDto.getPhone();
        this.message = message;
    }
}