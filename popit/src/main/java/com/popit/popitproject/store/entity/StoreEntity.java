package com.popit.popitproject.store.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.popit.popitproject.news.entity.NewsEntity;
import com.popit.popitproject.store.model.StoreType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "store")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 매장 주인(사업자)
    @OneToOne
    @JoinColumn(name = "seller_id")
    private StoreBusinessEntity seller;

    @Column
    private String storeName; // 사업자 가게명

    @Column
    @Enumerated(EnumType.STRING)
    private StoreType storeType; // 사업 종류

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

    private String storeAddress; // 매장 주소

    // map 등록을 위한 위경도
    @Column(name = "x")
    private Double x;

    @Column(name = "y")
    private Double y;

    // 소식작성
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "store") // 수정된 매핑 정보
    private List<NewsEntity> news;

}