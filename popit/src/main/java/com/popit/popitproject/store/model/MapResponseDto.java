package com.popit.popitproject.store.model;

import com.popit.popitproject.store.entity.StoreEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MapResponseDto{
    private Long id;
    private String storeName;
    private StoreType storeType;
    private String storeAddress;
    private Double x;
    private Double y;
    private String storePhone;
    private LocalTime openTime;
    private LocalTime closeTime;
    private LocalDate openDate;
    private LocalDate closeDate;

    public MapResponseDto(StoreEntity map){
        this.id = map.getId();
        this.storeName = map.getStoreName();
        this.storeType = map.getStoreType();
        this.storeAddress = map.getStoreAddress();
        this.x = map.getX();
        this.y = map.getY();
        this.storePhone = map.getStorePhone();
        this.openTime = map.getOpenTime();
        this.closeTime = map.getCloseTime();
        this.openDate = map.getOpenDate();
        this.closeDate = map.getCloseDate();
    }
}
