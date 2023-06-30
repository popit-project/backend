package com.popit.popitproject.store.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.store.exception.KakaoAddressChange;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StoreSellerDTO {
    private Long id;

    private String storeName;

    private String storeImgURL;

    private String storeType; // 사업 종류

    private String storeAddress; // 매장 주소

    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime; // 운영시간

    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate openDate; // 운영기간

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate closeDate;

    private UserEntity user;

    @NotBlank
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{5}")
    private String businessLicenseNumber;

    private Double x;
    private Double y;

    // 유저 아이디가 따로 없는 이유는 스프링 시큐리티를 이용해 인증을 구현하여서 유저가 자기 아이디를 넘겨주지 않아도 인증이가능하다.
    public static StoreEntity toEntity(final StoreSellerDTO dto) throws IOException {

        String newAddress = dto.getStoreAddress();
        StoreEntity change = KakaoAddressChange.addressChange(newAddress);

        return StoreEntity.builder()
                .id(dto.getId())
                .storeName(dto.getStoreName())
                .image(dto.getStoreImgURL())
                .storeType(StoreType.valueOf(dto.getStoreType()))
                .storeAddress(dto.getStoreAddress())
                .openTime(dto.getOpenTime())
                .closeTime(dto.getCloseTime())
                .openDate(dto.getOpenDate())
                .closeDate(dto.getCloseDate())
                .businessLicenseNumber(dto.getBusinessLicenseNumber())
                .x(change.getX())
                .y(change.getY())
                .user(dto.getUser())
                .build();
    }
}