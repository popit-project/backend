package com.popit.popitproject.store.controller;

import com.popit.popitproject.store.model.StoreCreateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreCreateResponse {

    private Long id; // 스토어 이름
    private String storeId;
    private String storeName; // 스토어 이름
    private String storeType; // 사업 종류
    private String storeLocation; // 스토어 주소
    private String businessLicenseNumber; // 사업자 등록번호
    private String businessLicenseImage; // 사업자 등록증 이미지

    public static StoreCreateResponse fromDTO(StoreCreateDTO storeCreateDTO) {
        return new StoreCreateResponse(
            storeCreateDTO.getId(),
            storeCreateDTO.getStoreId(),
            storeCreateDTO.getStoreName(),
            storeCreateDTO.getStoreType(),
            storeCreateDTO.getStoreLocation(),
            storeCreateDTO.getBusinessLicenseNumber(),
            storeCreateDTO.getBusinessLicenseImage()
        );
    }

}