package com.popit.popitproject.store.repository;

import com.popit.popitproject.store.model.StoreType;

import java.time.LocalDate;
import java.time.LocalTime;

public interface MapMapping {
    Long getId();
    String getStoreName();
    StoreType getStoreType();
    String getStoreAddress();
    Double getX();
    Double getY();
    String getStorePhone();
    LocalTime getOpenTime();
    LocalTime getCloseTime();
    LocalDate getOpenDate();
    LocalDate getCloseDate();
}
