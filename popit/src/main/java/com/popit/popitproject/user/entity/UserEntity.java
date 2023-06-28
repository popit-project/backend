// package com.popit.popitproject.user.entity;

// import com.popit.popitproject.store.entity.LikeEntity;
// import com.popit.popitproject.store.entity.StoreEntity;
// import com.popit.popitproject.store.model.SellerModeButton;
// import lombok.Getter;
// import lombok.Setter;
// import javax.persistence.*;
// import java.util.ArrayList;
// import java.util.Date;
// import java.util.List;

// @Entity
// @Table(name = "users")
// @Getter
// @Setter
// public class UserEntity {

//     @Id
//     @Column(nullable = false, unique = true)
//     private String email;
    
//     @Column(nullable = false)
//     private String userId;

//     @Column(nullable = false)
//     private String password;

//     @Column(nullable = false)
//     private String phone;

//     private String token;

//     @Column(nullable = false)
//     private String nickname;

//     @ElementCollection(fetch = FetchType.EAGER)
//     @Enumerated(EnumType.STRING)
//     private List<Role> roles = new ArrayList<>();

//     public enum Role {
//         ROLE_USER,
//         ROLE_SELLER
//     }

//     @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//     @JoinColumn(name = "seller")
//     private StoreEntity store;

//     @Column
//     @Enumerated(EnumType.STRING)
//     private SellerModeButton sellerModeButton = SellerModeButton.BUTTON_DISPLAY_OFF;

//     @Column
//     private Date lastTokenUsed;

//     // 좋아요 기능을 위해 추가
//     @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//     private List<LikeEntity> likes = new ArrayList<>();
// }
