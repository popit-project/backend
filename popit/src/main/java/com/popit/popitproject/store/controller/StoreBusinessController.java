package com.popit.popitproject.store.controller;

import com.popit.popitproject.store.controller.request.EnteredRequest;
import com.popit.popitproject.store.entity.StoreBusinessEntity;
import com.popit.popitproject.common.ResponseDTO;
import com.popit.popitproject.store.model.SellerModeButton;
import com.popit.popitproject.store.service.StoreBusinessService;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.model.StoreBusinessEnteredDTO;
import com.popit.popitproject.store.repository.StoreRepository;
import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.repository.UserRepository;
import com.popit.popitproject.user.service.JwtTokenService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreBusinessController {

    private final StoreBusinessService storeBusinessService;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;


    /**
     * sellerEntered : 입점신청
     * @return CREATED(200, ok), storeEntity
     */
    // TODO : 로그인 정보 끌어옴
    @PostMapping("/entered")
    public ResponseEntity<?> sellerEntered(@RequestBody EnteredRequest enteredRequest) {

        // 유저 정보 찾기
        UserEntity user = userRepository.findByEmail("user3@na.com").orElseThrow(()->new RuntimeException("사용자 정보 없음"));
        StoreBusinessEnteredDTO storeBusinessEnteredDTO = enteredRequest.toDTO(enteredRequest);

        // 리퀘스트에서 가져온 값을 디티오에 가져옴
        StoreBusinessEntity storeBusiness = storeBusinessService.entered(storeBusinessEnteredDTO);

        // 유저 판매자 권한 세팅
        UserEntity seller = storeBusinessService.generateSellerRole(user, storeBusiness);

        // 변환된 이용해서 리스펀스를 초기화
        ResponseDTO<StoreBusinessEnteredResponse> responseDTO
            = ResponseDTO.<StoreBusinessEnteredResponse>builder()
            .data(storeBusinessService.findStoreBusinessBySellerId(seller))
            .build();
        // ResponseDto 리턴

        return ResponseEntity.ok().body(responseDTO);
    }


    /**
     * sellerMode : 판매자 모드 전환, 셀러 모드 전환 시 셀러 기능 접근 가능
     *
     * @return CREATED(200, ok), storeName
     */
    @GetMapping("/sellerMode/{sellerId}")
    public ResponseEntity<?> sellerMode(HttpServletRequest request, @PathVariable String sellerId) {

        UserEntity user = (UserEntity) request.getSession().getAttribute("user");

        // 판매자 모드로 전환
        storeBusinessService.switchToSellerMode(user);

        // 판매자용 토큰 발급
        String sellerToken = jwtTokenService.generateSellerToken(user.getSeller().getSellerId(), user.getEmail());

        // 전환된 셀러 정보에 접근하여 필요한 처리 수행
        StoreEntity store = storeRepository.findBySeller(user.getSeller())
            .orElseThrow(() -> new IllegalArgumentException("사용자의 매장을 찾을 수 없습니다."));

        String message = "로그인된 사용자: " + user.getEmail() + "\n"
            + "스토어 이름: " + store.getSeller().getStoreName() + "\n"
            + "판매자용 토큰 : " + sellerToken;

        return ResponseEntity.ok().body(message);
    }

    // TODO : test 필요, 유저모드로 전환시 판매자용 토큰 비동기 방식으로 만료 처리
    // 유저 모드로 다시 전환
    @GetMapping("/info")
    public ResponseEntity<?> isUserModeButtonVisible() {

        // 사용자 정보 조회
        UserEntity user = userRepository.findByEmail("b244wer3w@naver.com")
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 원래 유저 페이지였을 경우
        if (user.getSellerModeButton() != SellerModeButton.BUTTON_CLICK_TO_USER_MODE) {
            return ResponseEntity.badRequest().body("현재 페이지는 유저페이지입니다.");
        }

        storeBusinessService.switchToUserMode(user);

        return ResponseEntity.ok().body("유저 페이지로 접속했습니다.");
    }


}