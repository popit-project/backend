package com.popit.popitproject.store.controller;

import com.popit.popitproject.store.model.StoreBusinessEnteredDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreCreateResponse {

    private Long sellerId;
    private String storeName; // 스토어 이름
    private String storeType; // 사업 종류
    private String businessLicenseAddress; // 스토어 주소
    private String businessLicenseNumber; // 사업자 등록번호

    public static StoreCreateResponse fromDTO(StoreBusinessEnteredDTO storeBusinessEnteredDTO) {
        return new StoreCreateResponse(
            storeBusinessEnteredDTO.getSellerId(),
            storeBusinessEnteredDTO.getStoreName(),
            storeBusinessEnteredDTO.getStoreType(),
            storeBusinessEnteredDTO.getBusinessLicenseAddress(),
            storeBusinessEnteredDTO.getBusinessLicenseNumber()
        );
    }

}