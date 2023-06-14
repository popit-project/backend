package com.popit.popitproject.user.entity;

import com.popit.popitproject.store.entity.StoreEntity;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {
    @Id
    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    private String token;

    @Column(nullable = true)
    private String sellerId;


}