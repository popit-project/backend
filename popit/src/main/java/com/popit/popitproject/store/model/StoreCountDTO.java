package com.popit.popitproject.store.model;

import com.popit.popitproject.store.repository.MapMapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreCountDTO implements MapMapping {
    private Long id;
    private String storeName;
    private String storeImage;
    private StoreType storeType;
    private String storeAddress;
    private Double x;
    private Double y;
    private LocalTime openTime;
    private LocalTime closeTime;
    private LocalDate openDate;
    private LocalDate closeDate;
    private int reviewCount;
    private int likeCount;


    public Long getId() {
        return id;
    }

    public String getStoreName() {
        return storeName;
    }

    public StoreType getStoreType() {
        return storeType;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }


    public LocalTime getOpenTime() {
        return openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public int getLikeCount() {
        return likeCount;
    }
}
