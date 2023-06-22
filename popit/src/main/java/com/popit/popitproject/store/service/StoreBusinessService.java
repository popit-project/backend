package com.popit.popitproject.store.service;


import static com.popit.popitproject.store.model.SellerModeButton.BUTTON_CLICK_TO_USER_MODE;

import com.popit.popitproject.store.model.SellerModeButton;
import com.popit.popitproject.store.entity.StoreBusinessEntity;
import com.popit.popitproject.news.entity.NewsEntity;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.news.dto.NewsDto;
import com.popit.popitproject.store.model.StoreBusinessEnteredDTO;
import com.popit.popitproject.news.repository.NewsRepository;
import com.popit.popitproject.store.repository.StoreBusinessRepository;
import com.popit.popitproject.store.repository.StoreRepository;
import com.popit.popitproject.user.entity.Role;
import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.repository.UserRepository;
import com.popit.popitproject.user.service.JwtTokenService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreBusinessService {

    private final UserRepository userRepository;
    private final StoreBusinessRepository storeBusinessRepository;
    private final JwtTokenService jwtTokenService;
    private final NewsRepository newsRepository;
    private final StoreRepository storeRepository;

    // 입점신청
    public UserEntity generateSellerRole(UserEntity user, StoreBusinessEntity storeBusiness) {
        // 유저쪽에 셀러 권한 설정
        user.setSellerModeButton(SellerModeButton.BUTTON_DISPLAY_ON);

        // 유저 엔티티의 role 설정
        List<Role> roles = new java.util.ArrayList<>();
        roles.add(Role.SELLER);
        user.setRoles(roles);
        // 유저에 저장하기
        user.setSeller(storeBusiness);
        return userRepository.save(user);
    }

    public StoreBusinessEntity entered(StoreBusinessEnteredDTO storeBusinessEnteredDTO) {
        // StoreBusinessEntity에 사업자 정보 저장하기
        return storeBusinessRepository.save(
            storeBusinessEnteredDTO.toEntity());

    }

    // user를 통해서 seller의 사업자 정보를 가져오기
    public StoreBusinessEnteredDTO findStoreBusinessBySellerId(UserEntity user) {
        StoreBusinessEntity storeBusiness = storeBusinessRepository.findById(user.getSeller().getSellerId())
            .orElseThrow(() -> new RuntimeException("매장을 찾을 수 없습니다."));
        return storeBusiness.toDTO();
    }

    // 일반 유저 모드에서 판매자 모드로 전환
    public void switchToSellerMode(UserEntity user) {
        // 판매자 토큰 생성
        String sellerToken = jwtTokenService
            .generateSellerToken(user.getSeller().getSellerId(), user.getEmail());

        // 판매자 모드로 전환
        user.setSellerModeButton(SellerModeButton.BUTTON_CLICK_TO_SELLER_MODE);

        // 판매자 토큰 및 모드 변경 저장
        user.setToken(sellerToken);
        userRepository.save(user);
    }

    // 판매자 모드에서 일반 유저 모드 전환
    public void switchToUserMode(UserEntity user) {
        // 판매자 토큰 생성
        String userToken = jwtTokenService
            .generateUserToken(user.getEmail());

        // 유저 모드로 전환
        user.setSellerModeButton(BUTTON_CLICK_TO_USER_MODE);

        // 유저용 토큰 및 모드 변경 저장
        user.setToken(userToken);
        userRepository.save(user);

    }


    // 현재 유저 : 이메일로 가져오기
    public UserEntity getCurrentUser(String userEmail) {
        return userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new IllegalArgumentException("인증된 셀러를 찾을 수 없습니다."));
    }

    // 로그인 된 셀러 가져오기
    public StoreBusinessEntity getCurrentSeller(String userEmail) {
        UserEntity loggedInUser = getCurrentUser(userEmail); // 로그인된 사용자 정보 가져오기
        return loggedInUser.getSeller(); // 로그인된 사용자의 StoreBusinessEntity 가져오기
    }


}