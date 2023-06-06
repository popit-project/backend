package com.popit.popitproject.map.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity

public class MapEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String store_name;
    private String store_phone; //가게 연락처
    private String store_address; //가게 주소
    private String store_time; // 가게 운영시간
    private String latitude;
    private String longitude;

}
