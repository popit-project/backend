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
public class SellerProfileResponse {

    private String storeImage;
    private String storeName;
    private String storeType; // 사업 종류
    private String storeAddress; // 매장 주소
}
