package com.popit.popitproject.store.service;


import com.popit.popitproject.store.exception.Calculate;
import com.popit.popitproject.store.model.StoreType;
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

    @Transactional
    public MapMapping findMapStoreName(String storeName){
        MapMapping store = storeRepository.findByStoreName(storeName);
        return store;
    }

    @Transactional
    public List<MapMapping> findMapAll(){
        return storeRepository.findAllBy();
    }

    @Transactional
    public List<MapMapping> findMapType(StoreType storeType){
        return storeRepository.findByStoreType(storeType);
    }

    @Transactional
    public List<MapMapping> findStoreWithin5km(double userLat, double userLon) {
        double distanceLimit = 5; // 10km

        List<MapMapping> storesWithin5km = new ArrayList<>();

        List<MapMapping> allStores = storeRepository.findAllBy();

        for (MapMapping store : allStores) {
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