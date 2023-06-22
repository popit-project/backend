package com.popit.popitproject.store.controller;

import com.popit.popitproject.store.model.StoreBusinessEnteredDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreBusinessEnteredResponse {

    private Long sellerId; // 스토어 id
    private String storeName; // 스토어 이름
    private String storeType; // 사업 종류
    private String businessLicenseAddress; // 스토어 주소
    private String businessLicenseNumber; // 사업자 등록번호
    private String sellerName; // 사업자의 이름
    private String sellerPhone; // 사업자의 연락처

//    public String toJSON() {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.enable(SerializationFeature.INDENT_OUTPUT);
//            return mapper.writeValueAsString(this);
//        } catch (JsonProcessingException e) {
//            // JSON 직렬화 중에 오류가 발생한 경우 예외 처리
//            e.printStackTrace();
//            return null;
//        }
//    }
    public StoreBusinessEnteredResponse toResponse(StoreBusinessEnteredDTO storeBusinessEnteredDTO ) {
        StoreBusinessEnteredResponse response = new StoreBusinessEnteredResponse();
        response.setSellerId(storeBusinessEnteredDTO.getSellerId());
        response.setStoreName(storeBusinessEnteredDTO.getStoreName());
        response.setStoreType(storeBusinessEnteredDTO.getStoreType());
        response.setBusinessLicenseAddress(storeBusinessEnteredDTO.getBusinessLicenseAddress());
        response.setBusinessLicenseNumber(storeBusinessEnteredDTO.getBusinessLicenseNumber());
        return  response;
    }
}