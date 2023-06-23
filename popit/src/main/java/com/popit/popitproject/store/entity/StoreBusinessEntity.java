package com.popit.popitproject.store.entity;

import com.popit.popitproject.store.model.StoreBusinessEnteredDTO;
import com.popit.popitproject.store.model.StoreType;
import com.popit.popitproject.user.entity.UserEntity;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "store_business")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreBusinessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellerId;

    @OneToOne(mappedBy = "seller", fetch = FetchType.LAZY)
    private UserEntity user;

    @Column
    private String storeName; // 사업자 가게명

    @Column
    @Enumerated(EnumType.STRING)
    private StoreType storeType; // 사업 종류

    @Column
    private String businessLicenseAddress; // 사업자 등록증 내 주소

    @Column
    private String businessLicenseNumber; // 사업자 등록번호

    // 소식작성
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "store") // 수정된 매핑 정보
    private List<NewsEntity> news;

    @Column
    private Timestamp enteredAt; // 입점 날짜

    @PrePersist
    void createdAt() {
        this.enteredAt = Timestamp.from(Instant.now());
    }

    public StoreBusinessEnteredDTO toDTO() {
        StoreBusinessEnteredDTO dto = new StoreBusinessEnteredDTO();
        dto.setSellerId(this.sellerId);
        dto.setStoreName(this.storeName);
        dto.setStoreType(storeType.getDisplayName());
        dto.setBusinessLicenseAddress(this.businessLicenseAddress);
        dto.setBusinessLicenseNumber(this.businessLicenseNumber);
        // Set other fields as needed
        return dto;
    }
}
