package com.popit.popitproject.map.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapDTO {

    private Long id;
    private String storeName;
    private String storePhone;
    private String storeAddress; //가게 주소
    private String storeTime; // 가게 운영시간
    private Double latitude;
    private Double longitude;
}
