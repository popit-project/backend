package com.popit.popitproject.store.model;


import com.popit.popitproject.store.entity.StoreEntity;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MapRequestDto {
    private Long id;
    private String storeName;

    public StoreEntity toEntity(){
        StoreEntity map = StoreEntity.builder()
                .id(id)
                .storeName(storeName)
                .build();
        return map;
    }

}
