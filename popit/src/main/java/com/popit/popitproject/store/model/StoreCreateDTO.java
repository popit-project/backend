package com.popit.popitproject.store.model;

import com.popit.popitproject.store.entity.StoreEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreCreateDTO {

    private Long id; // 스토어 이름
    private String storeId;
    private String storeName; // 스토어 이름
    private String storeType; // 사업 종류
    private String storeLocation; // 스토어 주소
    private String businessLicenseNumber; // 사업자 등록번호
    private String businessLicenseImage; // 사업자 등록증 이미지

    public static StoreCreateDTO fromEntity(StoreEntity storeEntity) {
        return new StoreCreateDTO(
            storeEntity.getId(),
            storeEntity.getStoreId(),
            storeEntity.getStoreName(),
            storeEntity.getStoreType().getDisplayName(),
            storeEntity.getStoreLocation(),
            storeEntity.getBusinessLicenseNumber(),
            storeEntity.getBusinessLicenseImage()
        );
    }
}
