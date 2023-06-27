package com.popit.popitproject.store.controller.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnteredRequest {

    private String storeName; // 스토어 이름
    private String storeType; // 사업 종류
    private String businessLicenseAddress; // 스토어 주소
    private String businessLicenseNumber; // 사업자 등록번호
}
