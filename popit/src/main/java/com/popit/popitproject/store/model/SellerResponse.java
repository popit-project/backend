package com.popit.popitproject.store.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private LocalTime openTime;

    private LocalTime closeTime;

    private LocalDate openDate;

    private LocalDate closeDate;

    private String businessLicenseNumber;

    private Double x;
    private Double y;


}
