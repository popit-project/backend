package com.popit.popitproject.user.model;

import com.popit.popitproject.store.model.SellerModeButton;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String userId;
    private String password;
    private String passwordCheck;
    private String email;
    private String phone;
    private String nickname;
    private SellerModeButton sellerModeButton;
}