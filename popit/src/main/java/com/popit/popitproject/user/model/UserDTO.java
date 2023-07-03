package com.popit.popitproject.user.model;

import com.popit.popitproject.store.model.SellerModeButton;
import com.popit.popitproject.user.entity.UserEntity;
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
    private Long sellerId;

    public UserDTO() {}

    public UserDTO(UserEntity userEntity) {
        this.userId = userEntity.getUserId();
        this.password = userEntity.getPassword();
        this.email = userEntity.getEmail();
        this.phone = userEntity.getPhone();
        this.nickname = userEntity.getNickname();
        this.sellerModeButton = userEntity.getSellerModeButton();
        this.sellerId = userEntity.getSellerId();
    }
}

//import com.popit.popitproject.store.model.SellerModeButton;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//public class UserDTO {
//    private String userId;
//    private String password;
//    private String passwordCheck;
//    private String email;
//    private String phone;
//    private String nickname;
//    private SellerModeButton sellerModeButton;
//    private Long sellerId;
//}