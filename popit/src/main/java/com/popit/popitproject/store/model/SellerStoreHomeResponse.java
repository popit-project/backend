package com.popit.popitproject.store.model;

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
public class SellerStoreHomeResponse {
    private Long sellerId;
    private Long storeId;
    private String storeImage;
    private String storeName;
    private String storeType;
    private String storeAddress; // 매장 주소
    private LocalTime openTime; // 운영시간
    private LocalTime closeTime;
    private LocalDate openDate; // 운영기간
    private LocalDate closeDate;

}
