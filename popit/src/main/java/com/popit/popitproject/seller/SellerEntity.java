package com.popit.popitproject.seller;

import com.popit.popitproject.store.model.StoreCreateDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "seller_entity")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String storeId;

    @Column
    private String email; // 판매자 로그인 정보

    @Column
    private String password;

    @Column
    private String token;

    @Column
    @Enumerated(EnumType.STRING)
    private SellerModeButton sellerModeButton = SellerModeButton.BUTTON_DISPLAY_OFF;

    public void setStoreId(StoreCreateDTO store) {
        this.storeId = store.getStoreId();
    }


}