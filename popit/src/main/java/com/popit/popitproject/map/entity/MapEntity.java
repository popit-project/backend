package com.popit.popitproject.map.entity;

import lombok.*;
import org.hibernate.mapping.Map;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "map")
@EntityListeners(AuditingEntityListener.class)
public class MapEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = true,name = "storeName")
    private String storeName;

    private String storePhone; //가게 연락처
    private String storeAddress; //가게 주소
    private String storeTime; // 가게 운영시간
    private Double latitude;
    private Double longitude;

}
