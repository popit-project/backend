package com.popit.popitproject.store.entity;

import com.popit.popitproject.news.entity.NewsEntity;
import com.popit.popitproject.review.entity.ReviewEntity;
import com.popit.popitproject.store.exception.storeSeller.StoreRegisteredException;
import com.popit.popitproject.store.model.StoreType;
import com.popit.popitproject.store.repository.MapMapping;
import com.popit.popitproject.user.entity.UserEntity;
import javax.validation.Constraint;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.FieldNameConstants;


@Getter
@Setter
@Entity(name = "store")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "store", uniqueConstraints = @UniqueConstraint(columnNames = "store_name"))
public class StoreEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 입점 신청 정보
    @Column(name = "store_name", unique = true) // Added unique constraint
    @NotBlank(message = "스토어 이름은 필수 입력 항목입니다.")
    private String storeName;

    @Lob
    private String storeImage;

    @Column
    @Enumerated(EnumType.STRING)
    private StoreType storeType;

    @Column(name = "store_address")
    @NotBlank(message = "가게 주소는 필수 입력 항목입니다.")
    private String storeAddress;

    @NotNull(message = "오픈 시간은 필수 입력 항목입니다.")
    private LocalTime openTime;

    @NotNull(message = "마감 시간은 필수 입력 항목입니다.")
    private LocalTime closeTime;

    @FutureOrPresent(message = "오픈 시간은 현재 또는 이후 날짜만 입력할 수 있습니다.")
    @NotNull(message = "운영기간은 필수 입력 항목입니다.")
    private LocalDate openDate;

    @FutureOrPresent(message = "운영 마감은 현재 또는 이후 날짜만 입력할 수 있습니다.")
    @NotNull(message = "운영 마감일은 필수 입력 항목입니다.")
    private LocalDate closeDate;

    @PrePersist
    private void prePersist() {
        if (openDate != null && closeDate != null && closeDate.isBefore(openDate)) {
            throw new StoreRegisteredException("Invalid close date");
        }
    }

    @Column
    private Timestamp updatedAt;

    @NotBlank
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{5}", message = "0000-00-00000의 형식으로 작성해주세요.")
    private String businessLicenseNumber;

    @Column
    private String storePhone;

    // 유저
    @OneToOne
    @JoinColumn(name = "seller", unique = true)
    private UserEntity user;

    // 소식
    @OneToMany(mappedBy = "seller")
    private List<NewsEntity> news;

    // map 위경도
    @Column(name = "x", nullable = true)
    private Double x;

    @Column(name = "y")
    private Double y;

    // 리뷰
    @OneToMany(mappedBy = "store")
    private List<ReviewEntity> comments;

    // 좋아요
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<LikeEntity> likes = new ArrayList<>();

    public static StoreEntity from(MapMapping mapMapping) {
        return StoreEntity.builder()
            .id(mapMapping.getId())
            .storeName(mapMapping.getStoreName())
            .build();
    }

}