package com.popit.popitproject.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String userId;
    private String password;
    private String nickname;
    private String email;
    private String phone;
}
