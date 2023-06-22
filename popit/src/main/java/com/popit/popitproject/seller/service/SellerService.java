package com.popit.popitproject.seller.service;


import static com.popit.popitproject.seller.SellerModeButton.BUTTON_CLICK_TO_SELLER_MODE;
import static com.popit.popitproject.seller.SellerModeButton.BUTTON_CLICK_TO_USER_MODE;

import com.popit.popitproject.seller.CustomSellerDetails;
import com.popit.popitproject.seller.CustomSellerDetailsService;
import com.popit.popitproject.seller.SellerEntity;
import com.popit.popitproject.seller.SellerModeButton;
import com.popit.popitproject.seller.SellerRepository;
import com.popit.popitproject.store.entity.NewsEntity;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.model.NewsDto;
import com.popit.popitproject.store.model.StoreCreateDTO;
import com.popit.popitproject.store.repository.NewsRepository;
import com.popit.popitproject.store.repository.StoreRepository;
import java.util.Objects;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final StoreRepository storeRepository;
    private final SellerRepository sellerRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final NewsRepository newsRepository;
    private final CustomSellerDetailsService customSellerDetailsService;
    private final BCryptPasswordEncoder encoder;

    // 입점신청
    public StoreCreateDTO entered(
        String storeName, String storeType, String storeLocation,
        String businessLicenseNumber, String businessLicenseImage
    ) {
        // 입점신청 진행 = DB 저장
        StoreEntity store = storeRepository.save(StoreEntity.of(
            storeName, storeType, storeLocation, businessLicenseNumber, businessLicenseImage
        ));

        return StoreCreateDTO.fromEntity(store);
    }

    // seller 권한 발급 : 아이디와 버튼 생성(BUTTON_DISPLAY_ON)
    public void generateSellerRole(SellerEntity seller, StoreCreateDTO storeCreateDTO) {
        // 셀러 아이디 발급
        seller.setStoreId(storeCreateDTO);
        seller.setSellerModeButton(SellerModeButton.BUTTON_DISPLAY_ON);
        sellerRepository.save(seller);
    }

    // 일반 유저 모드에서 판매자 모드 전환
    public void switchToSellerMode(SellerEntity seller) {
        seller.setSellerModeButton(BUTTON_CLICK_TO_SELLER_MODE);
        sellerRepository.save(seller);
    }

    // 판매자 모드에서 일반 유저 모드 전환
    public void switchToUserMode(SellerEntity seller) {
        seller.setSellerModeButton(BUTTON_CLICK_TO_USER_MODE);
        sellerRepository.save(seller);
    }


    public String authenticateSeller(String email, String password) {
        // 유저의 아이디, 비밀번호로 셀러용 토큰을 발급함
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );

        // Set the authentication object in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Get the authenticated user's email from the authentication object
        String authenticatedEmail = authentication.getName();

        // Generate a seller token

        return jwtTokenProvider.generateSellerToken(authenticatedEmail);
    }


    // [임시 테스트용 메서트] 유저 로그인시 유저용 토큰 발급
    public String login(String email, String password) {

        // 이메일로 사용자 정보 조회하기
        SellerEntity loginUser
            = sellerRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("없는 유저입니다."));

        // 비밀번호 확인하기
        if (!encoder.matches(password, loginUser.getPassword())) {
            throw new RuntimeException("로그인 정보가 일치하지 않습니다.");
        }

        // 유저용 토큰 발급
        return jwtTokenProvider.generateUserToken(email);
    }


    // 입점신청
    public NewsDto addNews(
        String title, String content
    ) {
        // 입점신청 진행 = DB 저장
        NewsEntity news = newsRepository.save(NewsEntity.of(
            title, content
        ));

        return NewsDto.fromEntity(news);
    }
}
