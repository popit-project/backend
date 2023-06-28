package com.popit.popitproject.store.service;


import com.popit.popitproject.review.entity.ReviewEntity;
import com.popit.popitproject.review.repository.ReviewRepository;
import com.popit.popitproject.store.entity.LikeEntity;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.exception.Calculate;
import com.popit.popitproject.store.model.StoreCountDTO;
import com.popit.popitproject.store.model.StoreType;
import com.popit.popitproject.store.repository.LikeRepository;
import com.popit.popitproject.store.repository.MapMapping;
import com.popit.popitproject.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final LikeRepository likeRepository;
    private final ReviewRepository reviewRepository;

//    @Transactional
//    public MapMapping findMapStoreName(String storeName){
//        MapMapping store = storeRepository.findByStoreName(storeName);
//        return store;
//    }

    @Transactional
    public List<StoreCountDTO> findMapAll(){
        List<MapMapping> stores = storeRepository.findAllBy();
        List<StoreCountDTO> mapMappings = new ArrayList<>();
        for (MapMapping store : stores) {
            Long storeId = store.getId();
            List<ReviewEntity> reviewCount = reviewRepository.findByStoreId(storeId);
            List<LikeEntity> likeCount = likeRepository.findByStore(StoreEntity.from(store));
            StoreCountDTO mapMapping = new StoreCountDTO(store.getId(), store.getStoreName(),store.getStoreImage(), store.getStoreType(),
                    store.getStoreAddress(), store.getX(), store.getY(),  store.getOpenTime(),
                    store.getCloseTime(), store.getOpenDate(), store.getCloseDate(), reviewCount.size(),likeCount.size());
            mapMappings.add(mapMapping);
        }
        return mapMappings;
    }

    @Transactional
    public List<StoreCountDTO> findMapType(StoreType storeType){
        List<MapMapping> stores = storeRepository.findByStoreType(storeType);
        List<StoreCountDTO> mapMappings = new ArrayList<>();
        for (MapMapping store : stores) {
            Long storeId = store.getId();
            List<ReviewEntity> reviewCount = reviewRepository.findByStoreId(storeId);
            List<LikeEntity> likeCount = likeRepository.findByStore(StoreEntity.from(store));
            StoreCountDTO mapMapping = new StoreCountDTO(store.getId(), store.getStoreName(),store.getStoreImage(), store.getStoreType(),
                    store.getStoreAddress(), store.getX(), store.getY(),  store.getOpenTime(),
                    store.getCloseTime(), store.getOpenDate(), store.getCloseDate(),reviewCount.size(),likeCount.size());
            mapMappings.add(mapMapping);
        }
        return mapMappings;

    }

    @Transactional
    public List<MapMapping> findStoreWithin5km(double userLat, double userLon) {
        double distanceLimit = 5; // 10km

        List<MapMapping> storesWithin5km = new ArrayList<>();

        List<MapMapping> stores = storeRepository.findAllBy();

        List<StoreCountDTO> mapMappings = new ArrayList<>();

        for (MapMapping store : stores) {
            Long storeId = store.getId();
            List<ReviewEntity> reviewCount = reviewRepository.findByStoreId(storeId);
            List<LikeEntity> likeCount = likeRepository.findByStore(StoreEntity.from(store));
            StoreCountDTO mapMapping = new StoreCountDTO(store.getId(), store.getStoreName(),store.getStoreImage(), store.getStoreType(),
                    store.getStoreAddress(), store.getX(), store.getY(),  store.getOpenTime(),
                    store.getCloseTime(), store.getOpenDate(), store.getCloseDate(), reviewCount.size(),likeCount.size());
            mapMappings.add(mapMapping);
        }

        for (MapMapping store : mapMappings) {
            double storeLat = store.getX();
            double storeLon = store.getY();

            double distance = Calculate.calculateDistance(userLat, userLon, storeLat, storeLon);

            if (distance <= distanceLimit) {
                storesWithin5km.add(store);
            }
        }
        return storesWithin5km;
    }

}
