package com.popit.popitproject.map.service;

import com.popit.popitproject.map.entity.MapEntity;
import com.popit.popitproject.map.repository.MapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class MapService {
    private final MapRepository mapRepository;

    @Transactional
    public MapEntity creatMap(Long id,String store_address,
                              String store_name,String store_phone,String store_time,
                              String longitude,String latitude){

        MapEntity save = mapRepository.save(
                MapEntity.builder()
                        .id(id)
                        .store_address(store_address)
                        .store_name(store_name)
                        .store_phone(store_phone)
                        .store_time(store_time)
                        .longitude(longitude)
                        .latitude(latitude)
                        .build()
        );
        return save;
    }

}
