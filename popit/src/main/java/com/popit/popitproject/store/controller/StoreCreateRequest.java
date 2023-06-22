package com.popit.popitproject.store.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreCreateRequest {

    private String storeName; // 스토어 이름
    private String storeType; // 사업 종류
    private String businessLicenseAddress; // 스토어 주소
    private String businessLicenseNumber; // 사업자 등록번호

}