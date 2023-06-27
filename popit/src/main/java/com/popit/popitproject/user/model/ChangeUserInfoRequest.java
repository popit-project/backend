package com.popit.popitproject.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeUserInfoRequest {
    private String email;
    private String newNickname;

    // 추가 가능
}
