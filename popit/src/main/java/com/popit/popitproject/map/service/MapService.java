package com.popit.popitproject.map.service;

import com.popit.popitproject.map.entity.MapEntity;
import com.popit.popitproject.map.repository.MapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MapService {

    private final MapRepository mapRepository;

    @Transactional
    public MapEntity creatMap(Long id,String storeAddress,
                              String storeName,String storePhone,String storeDay,String storeTime,
                              Double longitude,Double latitude){

        MapEntity save = mapRepository.save(
                MapEntity.builder()
                        .id(id)
                        .storeAddress(storeAddress)
                        .storeName(storeName)
                        .storePhone(storePhone)
                        .storeDay(storeDay)
                        .storeTime(storeTime)
                        .longitude(longitude)
                        .latitude(latitude)
                        .build()
        );
        validateDuplicateStoreName(save);
        return save;

    }

    private void validateDuplicateStoreName(MapEntity storeName){
        Optional<MapEntity> findName = mapRepository.findByStoreName(storeName.getStoreName());
//        if(findName.isEmpty()){
//            throw new IllegalStateException("이미 존재하는 마켓입니다.");
//        }
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
