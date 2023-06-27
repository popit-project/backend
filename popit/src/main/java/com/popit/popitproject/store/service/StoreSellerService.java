package com.popit.popitproject.store.service;

import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.model.SellerModeButton;
import com.popit.popitproject.store.repository.StoreSellerRepository;
import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.entity.UserEntity.Role;
import com.popit.popitproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 서비스 레이어는 컨트롤러와 퍼시스턴스 사이에서 비즈니스 로직을 수행하는 역할을 한다. HTTP와 긴밀히 연관된 컨트롤러에서 분리돼 있고, 또 데이터 베이스와 긴밀히 연관된
 * 퍼시스턴스와도 분리돼 있다.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreSellerService {

    private final StoreSellerRepository sellerRepository;
    private final UserRepository userRepository;

    public StoreEntity saveSellerInfo(UserEntity user, StoreEntity store) throws IOException {



        return sellerRepository.save(StoreEntity.builder()
                .id(store.getId())
                .storeName(store.getStoreName())
                .storeImage(store.getStoreImage())
                .storeType(store.getStoreType())
                .storeAddress(store.getStoreAddress())
                .openTime(store.getOpenTime())
                .closeTime(store.getCloseTime())
                .openDate(store.getOpenDate())
                .closeDate(store.getCloseDate())
                .user(user)
                .x(store.getX())
                .y(store.getY())
                .build()
        );
    }

    public void generateSellerRole(UserEntity user, StoreEntity store) {
        user.getRoles().add(Role.ROLE_SELLER);
        user.setSellerModeButton(SellerModeButton.BUTTON_DISPLAY_ON);
        user.setStore(store);
        userRepository.save(user);
    }

    // 일반 유저 모드에서 판매자 모드로 전환
    public void switchToSellerMode(UserEntity user) {
        // 판매자 모드로 전환
        user.setSellerModeButton(SellerModeButton.BUTTON_CLICK_TO_SELLER_MODE);
        userRepository.save(user);
    }

    // 판매자 모드에서 일반 유저 모드 전환
    public void switchToUserMode(UserEntity user) {
        // 판매자 모드로 전환
        user.setSellerModeButton(SellerModeButton.BUTTON_CLICK_TO_USER_MODE);
        userRepository.save(user);

    }
}