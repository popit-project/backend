package com.popit.popitproject.store.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerResponse {

    private Long id;

    private String storeName;

    private String storeImage;

    private String storeType; // 사업 종류

    private String storeAddress; // 매장 주소

    private LocalTime openTime; // 운영시간

    private LocalTime closeTime;

    private LocalDate openDate; // 운영기간

    private LocalDate closeDate;

    private Double x;
    private Double y;


}
