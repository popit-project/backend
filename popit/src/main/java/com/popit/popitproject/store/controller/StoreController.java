package com.popit.popitproject.store.controller;

import com.popit.popitproject.store.entity.LikeEntity;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.exception.KakaoAddressChange;
import com.popit.popitproject.store.model.StoreType;
import com.popit.popitproject.store.repository.LikeRepository;
import com.popit.popitproject.store.repository.MapMapping;
import com.popit.popitproject.store.repository.StoreRepository;
import com.popit.popitproject.store.service.StoreService;
import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.repository.UserRepository;
import com.popit.popitproject.user.service.JwtTokenService;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/api/store")
public class StoreController {
    private final StoreService storeService;
    private final StoreRepository storeRepository;
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    @ApiOperation(
            value = "스토어 검색"
            , notes = "스토어 이름을 통해 일치한 스토어를 검색합니다.")
    @GetMapping("/search/{storeName}")
    public ResponseEntity<MapMapping> findMap(@PathVariable String storeName){
        Optional.ofNullable(Optional.ofNullable(storeRepository.findByStoreName(storeName))
                .orElseThrow(() -> new IllegalArgumentException("일치하는 스토어가 없습니다.")));

        return ResponseEntity.ok(storeService.findMapStoreName(storeName));
    }
    @ApiOperation(
            value = "스토어 정보 모두 찾기"
            , notes = "스토어에 대한 모든 정보가 추출됩니다.")
    @GetMapping("/searchAll")
    public ResponseEntity findMapAll(){
        return ResponseEntity.ok(storeService.findMapAll());
    }

    @ApiOperation(
            value = "스토어 타입별로 추출"
            , notes = "스토어 타입을 넣어 타입 별로 추출됩니다.")
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

    @ApiOperation(
            value = "스토어 주소 수정"
            , notes = "스토어 ID를 입력해서 그안에 있는 주소를 수정하고 위도경도를 추출합니다..")
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

    @ApiOperation(
            value = "사용자 주변5km 스토어 조회"
            , notes = "사용자의 위치정보를 가지고와 주변 5KM 내에 있는 스토어를 조회합니다.")
    @GetMapping("/searchAll/5km")
    public List<MapMapping> find5km(@RequestParam("userLat") Double userLat, @RequestParam("userLon") Double userLon){
        return storeService.findStoreWithin5km(userLat,userLon);
    }

    // 좋아요 기능
    @PostMapping("/{storeId}/toggle-like")
    public ResponseEntity<?> toggleLikeStore(@PathVariable Long storeId, @RequestHeader(name = "Authorization") String token) {
        StoreEntity store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        String userEmail = jwtTokenService.getEmailFromToken(token.substring(7));
        UserEntity user = userRepository.findByEmail(userEmail);
        LikeEntity like = likeRepository.findByUserAndStore(user, store).orElse(null);

        if (like == null) {
            like = new LikeEntity(user, store);
            likeRepository.save(like);
            return ResponseEntity.ok("좋아요 되었습니다.");
        } else {
            likeRepository.delete(like);
            return ResponseEntity.ok("좋아요가 취소되었습니다.");
        }
    }
}