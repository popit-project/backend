package com.popit.popitproject.store.controller;

import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.exception.KakaoAddressChange;
import com.popit.popitproject.store.model.SellerResponse;
import com.popit.popitproject.store.model.StoreSellerDTO;
import com.popit.popitproject.store.model.UpdateStoreSellerDTO;
import com.popit.popitproject.store.repository.StoreRepository;
import com.popit.popitproject.store.repository.StoreSellerRepository;
import com.popit.popitproject.store.service.StoreSellerService;
import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.repository.UserRepository;
import java.io.IOException;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreSellerController {

    private final UserRepository userRepository;
    private final StoreSellerService sellerService;
    private final StoreSellerRepository storeRepository;

    @PostMapping("/sellerEnter")
    public ResponseEntity<?> sellerEntered(@AuthenticationPrincipal String userId,
        @RequestBody StoreSellerDTO sellerDTO) throws IOException {

        // 토큰에서 가지고 온 유저
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 이미 해당 유저가 판매자로 등록된 경우 예외 처리
        if (user.getStore() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 스토어가 등록된 상태입니다.");
        }

        // 서비스를 이용해서 Seller 생성
        StoreEntity createdSeller = sellerService.saveSellerInfo(user,
            StoreSellerDTO.toEntity(sellerDTO));
        sellerService.generateSellerRole(user, createdSeller);

        String newAddress = createdSeller.getStoreAddress();
        StoreEntity change = KakaoAddressChange.addressChange(newAddress);

        SellerResponse sellerResponse = SellerResponse.builder()
            .id(createdSeller.getId())
            .storeName(createdSeller.getStoreName())
            .storeImage(createdSeller.getStoreImage())
            .storeType(String.valueOf(createdSeller.getStoreType()))
            .storeAddress(createdSeller.getStoreAddress())
            .openTime(createdSeller.getOpenTime())
            .closeTime(createdSeller.getCloseTime())
            .openDate(createdSeller.getOpenDate())
            .closeDate(createdSeller.getCloseDate())
            .x(change.getX())
            .y(change.getY())
            .build();


        return ResponseEntity.ok().body(sellerResponse);
    }

    // TODO: 입점했던 정보를 수정하기
    @PutMapping("/sellerEnter")
    public ResponseEntity<?> updateSellerInfo(@AuthenticationPrincipal String userId,
        @RequestBody UpdateStoreSellerDTO updatedStore) throws IOException {


        // 토큰에서 가져온 사용자 정보
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        StoreEntity storeInfo = storeRepository.findByUser(user)
            .orElseThrow(() -> new IllegalArgumentException("Seller not found"));

        String newAddress = updatedStore.getStoreAddress();
        StoreEntity change =KakaoAddressChange.addressChange(newAddress);

        // 수정된 정보로 업데이트
        storeInfo.setStoreAddress(updatedStore.getStoreAddress());
        storeInfo.setOpenTime(updatedStore.getOpenTime());
        storeInfo.setCloseTime(updatedStore.getCloseTime());
        storeInfo.setOpenDate(updatedStore.getOpenDate());
        storeInfo.setCloseDate(updatedStore.getCloseDate());

        storeInfo.setX(change.getX());
        storeInfo.setY(change.getY());

        StoreEntity updatedStoreInfo = storeRepository.save(storeInfo);

        SellerResponse sellerResponse = SellerResponse.builder()
            .id(updatedStoreInfo.getId())
            .storeName(updatedStoreInfo.getStoreName())
            .storeImage(updatedStoreInfo.getStoreImage())
            .storeType(String.valueOf(updatedStoreInfo.getStoreType()))
            .storeAddress(updatedStoreInfo.getStoreAddress())
            .openTime(updatedStoreInfo.getOpenTime())
            .closeTime(updatedStoreInfo.getCloseTime())
            .openDate(updatedStoreInfo.getOpenDate())
            .closeDate(updatedStoreInfo.getCloseDate())

            .build();

        return ResponseEntity.ok().body(sellerResponse);
    }

    @GetMapping("/sellerMode")
    public ResponseEntity<?> sellerMode(@AuthenticationPrincipal String userId) {

        // 토큰에서 가지고 온 유저
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 판매자 모드로 전환
        sellerService.switchToSellerMode(user);

        return ResponseEntity.ok().body("판매자 모드로 전환되었습니다. " + user.getSellerModeButton());
    }

    @GetMapping("/userMode")
    public ResponseEntity<?> userMode(@AuthenticationPrincipal String userId) {

        // 토큰에서 가지고 온 유저
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 판매자 모드로 전환
        sellerService.switchToUserMode(user);

        return ResponseEntity.ok().body("유저 모드로 전환되었습니다. " + user.getSellerModeButton());
    }

}