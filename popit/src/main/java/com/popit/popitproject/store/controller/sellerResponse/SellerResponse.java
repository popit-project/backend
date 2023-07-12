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
public class SellerResponse {

    private Long id;

    private String storeName;

    private String storeImage;

    private String storeType;

    private String storeAddress;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate openDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate closeDate;

    private String businessLicenseNumber;

    private Double x;
    private Double y;

    public static SellerResponse getSellerResponse(StoreEntity createdSeller) {
        return SellerResponse.builder()
            .id(createdSeller.getId())
            .storeName(createdSeller.getStoreName())
            .storeImage(createdSeller.getStoreImage())
            .storeType(String.valueOf(createdSeller.getStoreType()))
            .storeAddress(createdSeller.getStoreAddress())
            .openTime(createdSeller.getOpenTime())
            .closeTime(createdSeller.getCloseTime())
            .openDate(createdSeller.getOpenDate())
            .closeDate(createdSeller.getCloseDate())
            .businessLicenseNumber(createdSeller.getBusinessLicenseNumber())
            .build();
    }
}
