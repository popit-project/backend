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
public class StoreOpenDTO {

    private String image; // 매장 이미지

    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime; // 운영시간

    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate openDate; // 운영기간

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate closeDate;

    private String location; // 매장 주소

}