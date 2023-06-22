package com.popit.popitproject.store.model;

import com.popit.popitproject.store.entity.StoreBusinessEntity;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.model.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreBusinessEnteredDTO {

    private Long sellerId; // 스토어 id
    private String storeName; // 스토어 이름
    private String storeType; // 사업 종류
    private String businessLicenseAddress; // 스토어 주소
    private String businessLicenseNumber; // 사업자 등록번호

    public StoreBusinessEntity toEntity() {
        StoreBusinessEntity entity = new StoreBusinessEntity();
        entity.setSellerId(this.sellerId);
        entity.setStoreName(this.storeName);
        entity.setStoreType(StoreType.valueOf(this.storeType));
        entity.setBusinessLicenseAddress(this.businessLicenseAddress);
        entity.setBusinessLicenseNumber(this.businessLicenseNumber);
        // Set other fields as needed
        return entity;
    }


}
