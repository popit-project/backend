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
public class SellerStoreHomeResponse {

    private Long sellerId;
    private Long storeId;
    private String storeImage;
    private String storeName;
    private String storeType;
    private String storeAddress; // 매장 주소
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime; // 운영시간
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate openDate; // 운영기간
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate closeDate;


    public static SellerStoreHomeResponse getSellerStoreHomeByUserId(StoreEntity store) {
        return SellerStoreHomeResponse.builder()
            .sellerId(store.getId())
            .storeId(store.getId())
            .sellerId(store.getUser().getStore().getId())
            .storeImage(store.getStoreImage())
            .storeName(store.getStoreName())
            .storeType(store.getStoreType().getDisplayName())
            .storeAddress(store.getStoreAddress())
            .openTime(store.getOpenTime())
            .closeTime(store.getCloseTime())
            .openDate(store.getOpenDate())
            .closeDate(store.getCloseDate())
            .build();
    }

    public static SellerStoreHomeResponse getSellerStoreHomeResponseBySellerId(Long sellerId,
        StoreEntity store) {
        return SellerStoreHomeResponse.builder()
            .sellerId(sellerId)
            .storeId(store.getId())
            .sellerId(store.getUser().getStore().getId())
            .storeImage(store.getStoreImage())
            .storeName(store.getStoreName())
            .storeType(store.getStoreType().getDisplayName())
            .storeAddress(store.getStoreAddress())
            .openTime(store.getOpenTime())
            .closeTime(store.getCloseTime())
            .openDate(store.getOpenDate())
            .closeDate(store.getCloseDate())
            .build();
    }

    public static SellerStoreHomeResponse getSellerStoreHomeResponse(StoreEntity store) {
        return SellerStoreHomeResponse.builder()
            .storeId(store.getId())
            .sellerId(store.getUser().getStore().getId())
            .storeImage(store.getStoreImage())
            .storeName(store.getStoreName())
            .storeType(store.getStoreType().getDisplayName())
            .storeAddress(store.getStoreAddress())
            .openTime(store.getOpenTime())
            .closeTime(store.getCloseTime())
            .openDate(store.getOpenDate())
            .closeDate(store.getCloseDate())
            .build();
    }

}
