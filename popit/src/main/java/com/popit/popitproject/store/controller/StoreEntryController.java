package com.popit.popitproject.store.controller;


import com.popit.popitproject.common.exception.ResponseDTO;
import com.popit.popitproject.common.exception.StatusCode;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.exception.storeSeller.StoreSellerValidate;

import com.popit.popitproject.store.controller.sellerResponse.SellerResponse;
import com.popit.popitproject.store.controller.sellerResponse.SellerStoreHomeResponse;
import com.popit.popitproject.store.controller.sellerResponse.SellerUpdateResponse;
import com.popit.popitproject.store.model.SellerEntryDTO;

import com.popit.popitproject.store.model.UpdateStoreInfoDTO;
import com.popit.popitproject.store.repository.StoreSellerRepository;
import com.popit.popitproject.store.service.StoreSellerService;
import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.repository.UserRepository;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

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
@RequestMapping("/api")
public class StoreEntryController {

    private final UserRepository userRepository;
    private final StoreSellerService sellerService;
    private final StoreSellerRepository storeRepository;
    private final StoreSellerValidate storeSellerValidate;


    @ApiOperation(
        value = "판매자 입점신청"
        , notes = "로그인 후, 해당 사용자의 정보로 가게 입점신청을 진행하고 가게를 생성합니다.")
    @PostMapping(path = "/sellerEnter", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> sellerEntered(@AuthenticationPrincipal String userId,
        @RequestPart("file") MultipartFile file,
        @RequestPart("sellerDTO") String sellerDTO) throws IOException {

        SellerEntryDTO storeSellerDTO = SellerEntryDTO.getStoreSellerDTO(file, sellerDTO);

        UserEntity user = storeSellerValidate.validateSellerRegistration(userId, storeSellerDTO);

        StoreEntity createdSeller = sellerService.saveStoreInfo(user,storeSellerDTO);
        sellerService.generateSellerRole(user, createdSeller);

        SellerResponse sellerResponse = SellerResponse.getSellerResponse(createdSeller);

        ResponseDTO<Object> response =
            ResponseDTO.builder()
                .status(String.valueOf(StatusCode.SELLER_STORE_CREATE.getStatus()))
                .message(StatusCode.SELLER_STORE_CREATE.getMessage())
                .data(sellerResponse)
                .build();

        return ResponseEntity.ok().body(response);
    }


    @ApiOperation(
        value = "스토어 유저용 프로필 홈"
        , notes = "입점 신청 후 생성된 가게의 정보를 줍니다.")
    @GetMapping("/store/{storeId}/storeHome")
    public ResponseEntity<?> userStoreInfo(@PathVariable("storeId") Long storeId) {

        StoreEntity store = storeRepository.findById(storeId).orElseThrow(
            () -> new RuntimeException("매장을 찾을 수 없습니다.")
        );

        SellerStoreHomeResponse sellerResponse = SellerStoreHomeResponse.getSellerStoreHomeResponse(
            store);

        ResponseDTO<Object> response =
            ResponseDTO.builder()
                .status(String.valueOf(StatusCode.SELLER_STORE_UPDATE.getStatus()))
                .message(StatusCode.SELLER_STORE_UPDATE.getMessage())
                .data(sellerResponse)
                .build();

        return ResponseEntity.ok().body(response);
    }


    @ApiOperation(
        value = "스토어 셀러용 프로필 홈"
        , notes = "입점 신청 후 생성된 가게의 정보를 줍니다.")
    @GetMapping("/seller/{sellerId}/storeHome")
    public ResponseEntity<?> sellerStoreInfo(@AuthenticationPrincipal String userId,
        @PathVariable("sellerId") Long sellerId) {

        UserEntity user = userRepository.findByUserId(userId);
        StoreEntity store = storeRepository.findById(user.getStore().getId()).orElseThrow(
            () -> new RuntimeException("매장을 찾을 수 없습니다.")
        );

        SellerStoreHomeResponse sellerResponse = SellerStoreHomeResponse.getSellerStoreHomeResponseBySellerId(
            sellerId, store);

        return ResponseEntity.ok().body(sellerResponse);

    }

    @ApiOperation(
        value = "가게 삭제"
        , notes = "가게를 삭제하고, 그 안의 리뷰, 소식, 아이템도 함께 삭제합니다.")
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
        value = "판매자 입점 정보 수정",
        notes = "가게 입점 정보를 수정합니다.")
    @PutMapping(path = "/seller/sellerEnter")
    public ResponseEntity<?> updateSellerInfo(@AuthenticationPrincipal String userId,
        @RequestBody UpdateStoreInfoDTO updateStoreInfoDTO) throws IOException {

        // 토큰에서 가져온 사용자 정보
        UserEntity user = userRepository.findByUserId(userId);

        StoreEntity originStoreInfo = storeRepository.findByUser(user)
            .orElseThrow(() -> new IllegalArgumentException("Seller not found"));

        // 스토어 정보 업데이트
        StoreEntity updatedStoreInfo = sellerService.updateStore(updateStoreInfoDTO, originStoreInfo);

        SellerUpdateResponse sellerUpdateResponse = SellerUpdateResponse.getSellerUpdateResponse(
            originStoreInfo, updatedStoreInfo);

        return ResponseEntity.ok().body(sellerUpdateResponse);
    }



    @ApiOperation(
            value = "유저 id로 스토어 받아오기"
            , notes = "입점 신청 후 생성된 가게의 정보를 줍니다.")
    @GetMapping("/seller/storeHome")
    public ResponseEntity<?> storeInfoByUser(@AuthenticationPrincipal String userId) {

        UserEntity user = userRepository.findByUserId(userId);
        StoreEntity store = storeRepository.findByUser(user).orElseThrow(
                () -> new RuntimeException("매장을 찾을 수 없습니다.")
        );

        SellerStoreHomeResponse sellerResponse = SellerStoreHomeResponse.getSellerStoreHomeByUserId(
            store);

        return ResponseEntity.ok().body(sellerResponse);
    }


}
