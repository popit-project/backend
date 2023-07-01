package com.popit.popitproject.store.controller;


import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.exception.KakaoAddressChange;
import com.popit.popitproject.store.exception.storeSeller.StoreSellerValidate;

import com.popit.popitproject.store.model.SellerResponse;
import com.popit.popitproject.store.model.SellerStoreHomeResponse;
import com.popit.popitproject.store.model.StoreSellerDTO;

import com.popit.popitproject.store.model.UpdateStoreSellerDTO;
import com.popit.popitproject.store.repository.StoreSellerRepository;
import com.popit.popitproject.store.service.StoreSellerService;
import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@ControllerAdvice(assignableTypes = StoreSellerController.class)
@RequestMapping("/api")
public class StoreSellerController {

    private final UserRepository userRepository;
    private final StoreSellerService sellerService;
    private final StoreSellerRepository storeRepository;
    private final StoreSellerValidate storeSellerValidate;


    @ApiOperation(
            value = "판매자 입점신청"
            , notes = "로그인 후, 해당 사용자의 정보로 가게 입점신청을 진행하고 가게를 생성합니다.")
    @PostMapping(path = "/sellerEnter", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> sellerEntered(@AuthenticationPrincipal String userId,
                                           @RequestPart(name = "file") MultipartFile file,
                                           @RequestPart(name = "sellerDTO") StoreSellerDTO sellerDTO) throws IOException {

        // 유효성 검사
        UserEntity user = storeSellerValidate.validateSellerRegistration(userId, sellerDTO);

        // 서비스를 이용해서 Seller 생성
        StoreEntity createdSeller = sellerService.saveSellerInfo(file, user,
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
                .businessLicenseNumber(createdSeller.getBusinessLicenseNumber())
                .x(change.getX())
                .y(change.getY())
                .build();
        return ResponseEntity.ok().body(sellerResponse);
    }


    @ApiOperation(
            value = "스토어 유저용 프로필 홈"
            , notes = "입점 신청 후 생성된 가게의 정보를 줍니다.")
    @GetMapping("/store/{storeId}/storeHome")
    public ResponseEntity<?> userStoreInfo(@PathVariable("storeId") Long storeId) {

        StoreEntity store = storeRepository.findById(storeId).orElseThrow(
                () -> new RuntimeException("매장을 찾을 수 없습니다.")
        );

        SellerStoreHomeResponse sellerResponse = SellerStoreHomeResponse.builder()
                .storeId(store.getId())
                .sellerId(store.getUser().getStore().getId())
                .storeImage(store.getStoreImage())
                .storeName(store.getStoreName())
                .storeType(store.getStoreType().getDisplayName())
                .storeAddress(store.getStoreAddress())
                .openTime(store.getOpenTime())
                .closeTime(store.getCloseTime())
                .openDate(store.getOpenDate())
                .closeDate(store.getCloseDate())
                .build();

        return ResponseEntity.ok().body(sellerResponse);

    }

    @ApiOperation(
            value = "스토어 셀러용 프로필 홈"
            , notes = "입점 신청 후 생성된 가게의 정보를 줍니다.")
    @GetMapping("가")
    public ResponseEntity<?> sellerStoreInfo(@AuthenticationPrincipal String userId,
                                             @PathVariable("sellerId") Long sellerId) {

        UserEntity user = userRepository.findByUserId(userId);
        StoreEntity store = storeRepository.findById(user.getStore().getId()).orElseThrow(
                () -> new RuntimeException("매장을 찾을 수 없습니다.")
        );

        SellerStoreHomeResponse sellerResponse = SellerStoreHomeResponse.builder()
                .sellerId(sellerId)
                .storeId(store.getId())
                .sellerId(store.getUser().getStore().getId())
                .storeImage(store.getStoreImage())
                .storeName(store.getStoreName())
                .storeType(store.getStoreType().getDisplayName())
                .storeAddress(store.getStoreAddress())
                .openTime(store.getOpenTime())
                .closeTime(store.getCloseTime())
                .openDate(store.getOpenDate())
                .closeDate(store.getCloseDate())
                .build();

        return ResponseEntity.ok().body(sellerResponse);

    }

    @ApiOperation(
            value = "가게 삭제"
            , notes = "가게를 삭제하고, 그 안의 리뷰, 소식도 함께 삭제합니다.")
    @DeleteMapping("/stores/{storeId}")
    public ResponseEntity<String> deleteStore(@PathVariable Long storeId) {
        try {
            // 스토어 삭제 로직 구현
            sellerService.deleteStore(storeId);

            return ResponseEntity.ok("Store deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete store: " + e.getMessage());
        }
    }

    @ApiOperation(
            value = "판매자 입점 정보 수정"
            , notes = "가게 입점 정보를 수정합니다.")
    @PutMapping("/seller/sellerEnter")
    public ResponseEntity<?> updateSellerInfo(@AuthenticationPrincipal String userId,
                                              @RequestBody UpdateStoreSellerDTO updatedStore) throws IOException {

        // 토큰에서 가져온 사용자 정보
        UserEntity user = userRepository.findByUserId(userId);

        StoreEntity storeInfo = storeRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Seller not found"));

        String newAddress = updatedStore.getStoreAddress();
        StoreEntity change = KakaoAddressChange.addressChange(newAddress);

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
                .businessLicenseNumber(updatedStoreInfo.getBusinessLicenseNumber())
                .build();

        return ResponseEntity.ok().body(sellerResponse);
    }


}