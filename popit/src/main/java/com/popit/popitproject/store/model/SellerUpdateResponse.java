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
public class SellerUpdateResponse {
    private Long storeId;

    private String storeAddress;

    private LocalTime openTime;

    private LocalTime closeTime;

    private LocalDate openDate;

    private LocalDate closeDate;

}