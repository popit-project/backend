package com.popit.popitproject.map.service;

import com.popit.popitproject.map.entity.MapEntity;
import com.popit.popitproject.map.repository.MapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MapService {
    private final MapRepository mapRepository;
    private static List<MapEntity> maps = new ArrayList<>();

    @Transactional
    public MapEntity creatMap(Long id,String storeAddress,
                              String storeName,String storePhone,String storeTime,
                              Double longitude,Double latitude){

        MapEntity save = mapRepository.save(
                MapEntity.builder()
                        .id(id)
                        .storeAddress(storeAddress)
                        .storeName(storeName)
                        .storePhone(storePhone)
                        .storeTime(storeTime)
                        .longitude(longitude)
                        .latitude(latitude)
                        .build()
        );
        return save;
    }
    @Transactional
    public MapEntity findMap(String storeName){
        MapEntity map = this.mapRepository.findByStoreName(storeName).orElse(null);

        return map;
    }

    @Transactional
    public List<MapEntity> findMapAll(){
        return mapRepository.findAll();
    }

}
