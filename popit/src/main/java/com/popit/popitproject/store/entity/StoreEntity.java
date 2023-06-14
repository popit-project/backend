package com.popit.popitproject.store.entity;

import com.popit.popitproject.seller.SellerEntity;
import com.popit.popitproject.store.model.StoreCreateDTO;
import com.popit.popitproject.store.model.StoreType;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Entity(name = "store_entity")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String storeId;

    @Column
    private String storeName; // 스토어 이름

    @Column
    @Enumerated(EnumType.STRING)
    private StoreType storeType; // 사업 종류

    @Column
    private String storeLocation; // 스토어 주소

    @Column
    private String businessLicenseNumber; // 사업자 등록번호

    @Column
    private String businessLicenseImage; // 사업자 등록증 이미지

    @OneToMany(mappedBy = "id")
    private List<NewsEntity> news;

    // 서비스 단에서 쓰기 위해 변환을 위한 메서드
    public static StoreEntity of(String storeName, String storeType, String storeLocation,
        String businessLicenseNumber, String businessLicenseImage) {

        StoreEntity storeEntity = new StoreEntity();

        storeEntity.setStoreName(storeName);
        storeEntity.setStoreType(StoreType.valueOf(storeType));
        storeEntity.setStoreLocation(storeLocation);
        storeEntity.setBusinessLicenseNumber(businessLicenseNumber);
        storeEntity.setBusinessLicenseImage(businessLicenseImage);
        storeEntity.setStoreId(UUID.randomUUID(), storeName);


        return storeEntity;
    }

    public void setStoreId(UUID uuid, String storeName) {
        int hashCode = uuid.hashCode();
        long absoluteValue = Math.abs((long) hashCode);
        // 스토어 이름과 UUID 해시값을 조합하여 storeId 생성
        this.storeId = storeName + absoluteValue;
    }

}

