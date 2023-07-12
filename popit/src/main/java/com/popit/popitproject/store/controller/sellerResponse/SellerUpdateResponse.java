package com.popit.popitproject.store.controller.sellerResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.popit.popitproject.store.entity.StoreEntity;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerUpdateResponse {

    private Long storeId;

    private String storeAddress;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate openDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate closeDate;

    public static SellerUpdateResponse getSellerUpdateResponse(StoreEntity originStoreInfo,
        StoreEntity updatedStoreInfo) {
        SellerUpdateResponse sellerUpdateResponse = SellerUpdateResponse.builder()
            .storeId(originStoreInfo.getId())
            .storeAddress(updatedStoreInfo.getStoreAddress())
            .openTime(updatedStoreInfo.getOpenTime())
            .closeTime(updatedStoreInfo.getCloseTime())
            .openDate(updatedStoreInfo.getOpenDate())
            .closeDate(updatedStoreInfo.getCloseDate())
            .build();
        return sellerUpdateResponse;
    }
}