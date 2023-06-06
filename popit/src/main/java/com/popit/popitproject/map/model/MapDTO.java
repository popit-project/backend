package com.popit.popitproject.map.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapDTO {

    private Long id;
    private String store_name;
    private String store_phone;
    private String store_address; //가게 주소
    private String store_time; // 가게 운영시간
    private String latitude;
    private String longitude;
}
