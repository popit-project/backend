package com.popit.popitproject.store.controller;

import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.exception.KakaoAddressChange;
import com.popit.popitproject.store.model.StoreType;
import com.popit.popitproject.store.repository.MapMapping;
import com.popit.popitproject.store.repository.StoreRepository;
import com.popit.popitproject.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.popit.popitproject.store.exception.Calculate.calculateDistance;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {
    private final StoreService storeService;
    private final StoreRepository storeRepository;

    @GetMapping("/search/{storeName}")
    public ResponseEntity<MapMapping> findMap(@PathVariable String storeName){
        Optional.ofNullable(Optional.ofNullable(storeRepository.findByStoreName(storeName))
                .orElseThrow(() -> new IllegalArgumentException("일치하는 스토어가 없습니다.")));

        return ResponseEntity.ok(storeService.findMapStoreName(storeName));

    }
    @GetMapping("/searchAll")
    public ResponseEntity findMapAll(@RequestParam(required = false) String storeName){

        if(storeName == null){
            return ResponseEntity.ok(storeService.findMapAll());
        }
        return ResponseEntity.ok(storeService.findMapStoreName(storeName));
    }

    @GetMapping("/searchType/{storeType}")
    public List<MapMapping> findMapType(@PathVariable StoreType storeType,
                                        @RequestParam("userLat") Double userLat,
                                        @RequestParam("userLon") Double userLon,
                                        @RequestParam(value = "radius", defaultValue = "5") int radius) {
        List<MapMapping> mapResults = storeService.findMapType(storeType);
        List<MapMapping> storesWithinRadius = new ArrayList<>();
        for (MapMapping store : mapResults) {
            double distance = calculateDistance(userLat, userLon, store.getX(), store.getY());
            if (distance <= radius) {
                storesWithinRadius.add(store);
            }
        }
        return ResponseEntity.ok(storesWithinRadius).getBody();
    }

    @PutMapping("/update/{storeId}/address")
    public ResponseEntity<String> updateStore(@PathVariable Long storeId,@RequestBody StoreEntity storeAddress) throws IOException {
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(()->new IllegalArgumentException(" No matching stores."));
        String newAddress = storeAddress.getStoreAddress();
        StoreEntity change =KakaoAddressChange.addressChange(newAddress);

        store.setStoreAddress(newAddress);
        store.setX(change.getX());
        store.setY(change.getY());
        storeRepository.save(store);
        return ResponseEntity.ok("Address update.");
    }

    @GetMapping("/searchAll/5km")
    public List<MapMapping> find5km(@RequestParam("userLat") Double userLat, @RequestParam("userLon") Double userLon){
        return storeService.findStoreWithin5km(userLat,userLon);
    }
}