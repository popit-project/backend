package com.popit.popitproject.store.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.popit.popitproject.review.entity.ReviewEntity;
import com.popit.popitproject.store.model.StoreType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Getter
@Setter
@Entity(name = "store")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "store_name")
    private String storeName;

    @Column
    @Enumerated(EnumType.STRING)
    private StoreType storeType; // 사업 종류

    @Column
    private Timestamp updatedAt;

    @Column
    private String storePhone;

    // 새로운 팝업에 대한 수정/등록
    private String image; // 매장 이미지

    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime; // 운영시간

    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate openDate; // 운영기간

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate closeDate;


    @Column(name = "store_address")
    private String storeAddress; // 매장 주소

    // map 등록을 위한 위경도
    @Column(name = "x")
    private Double x;

    @Column(name = "y")
    private Double y;


    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewEntity> comments;

}
