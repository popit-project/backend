package com.popit.popitproject.store.entity;


import com.popit.popitproject.news.entity.NewsEntity;
import com.popit.popitproject.review.entity.ReviewEntity;
import com.popit.popitproject.store.model.StoreType;
import com.popit.popitproject.store.repository.MapMapping;
import com.popit.popitproject.user.entity.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    @Column(name="store_name")
    private String storeName;

    private String storeImage;

    @Column
    @Enumerated(EnumType.STRING)
    private StoreType storeType; // 사업 종류

    @Column(name = "store_address")
    private String storeAddress; // 매장 주소

    private LocalTime openTime; // 운영시간

    private LocalTime closeTime;

    private LocalDate openDate; // 운영기간

    private LocalDate closeDate;


    @Column
    private Timestamp updatedAt;

    @Column
    private String storePhone;

    @OneToOne
    @JoinColumn(name = "seller", unique = true)
    private UserEntity user;

    @OneToMany(mappedBy = "seller")
    private List<NewsEntity> news;

    // map 등록을 위한 위경도
    @Column(name = "x")
    private Double x;

    @Column(name = "y")
    private Double y;


//    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ReviewEntity> comments;

    @OneToMany(mappedBy = "store")
    private List<ReviewEntity> comments;


    public static StoreEntity from (MapMapping mapMapping) {
        return StoreEntity.builder()
            .id(mapMapping.getId())
            .storeName(mapMapping.getStoreName())
            .build();
    }

    // 좋아요 기능을 위해 추가
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<LikeEntity> likes = new ArrayList<>();
}