package com.popit.popitproject.seller.controller;


import static com.popit.popitproject.seller.service.JwtTokenProvider.extractTokenFromRequest;

import com.popit.popitproject.seller.CustomSellerDetails;
import com.popit.popitproject.seller.CustomSellerDetailsService;
import com.popit.popitproject.seller.SellerEntity;
import com.popit.popitproject.seller.SellerModeButton;
import com.popit.popitproject.seller.SellerRepository;
import com.popit.popitproject.seller.service.JwtTokenProvider;
import com.popit.popitproject.seller.service.SellerService;
import com.popit.popitproject.store.controller.NewsRequest;
import com.popit.popitproject.store.controller.NewsResponse;
import com.popit.popitproject.store.controller.StoreCreateRequest;
import com.popit.popitproject.store.controller.StoreCreateResponse;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.model.NewsDto;
import com.popit.popitproject.store.model.StoreCreateDTO;
import com.popit.popitproject.store.repository.StoreRepository;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller")
public class SellerController {

    private final SellerService sellerService;
    private final SellerRepository sellerRepository;
    private final StoreRepository storeRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomSellerDetailsService sellerDetailsService;


    /**
     * createStore : 입점신청
     *
     * @param storeRequest : 스토어 정보 받기
     * @return CREATED(200, ok), storeEntity
     */
    // TODO : 로그인 정보 끌어옴
    @PostMapping("/entered")
    public ResponseEntity<?> sellerEntered(@RequestBody StoreCreateRequest storeRequest) {

        // 사용자 정보 조회
        SellerEntity seller = sellerRepository.findByEmail("b244wer3w@naver.com")
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 판매자 입점신청 진행 = DB 저장
        StoreCreateDTO store = sellerService.entered(
            storeRequest.getStoreName(),
            storeRequest.getStoreType(),
            storeRequest.getStoreLocation(),
            storeRequest.getBusinessLicenseNumber(),
            storeRequest.getBusinessLicenseImage()
        );

        // 셀러 아이디 발급, 셀러 엔티티에 셀러 아이디 저장
        sellerService.generateSellerRole(seller, store);

        return ResponseEntity.ok().body(StoreCreateResponse.fromDTO(store));
    }

    // [아래 테스트용으로 임시로 만들어둠]패스워드 수정 API
    @PutMapping("/{sellerId}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long sellerId) {

        // 사용자 정보 조회
        SellerEntity seller = sellerRepository.findById(sellerId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 새로운 패스워드 해싱
        String hashedPassword = new BCryptPasswordEncoder().encode(seller.getPassword());

        // 패스워드 업데이트
        seller.setPassword(hashedPassword);
        sellerRepository.save(seller);

        return ResponseEntity.ok(seller.getEmail() + "님의 패스워드가 성공적으로 업데이트되었습니다.");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        // 사용자 인증 로직 수행
        String token = sellerService.login(email, password);

        return ResponseEntity.ok().body(token);
    }


    // TODO : 판매자용 토큰을 따로 생성을 했지만, 유저용과 합쳐야 할지 고민해보기
    // 따로 하게된다면, 동기적인 방식으로 유저 로그인시와 유저 토큰 만료시 판매자용 토큰도 만료될 수 있도록 처리
    /**
     * sellerMode : 판매자 모드 전환, 셀러 모드 전환 시 셀러 기능 접근 가능
     *
     * @return CREATED(200, ok), storeName
     */
    @GetMapping("/sellerMode/{storeId}")
    public ResponseEntity<?> sellerMode(HttpServletRequest request, @PathVariable String storeId) {

        // 토큰 가져오기
        String token = extractTokenFromRequest(request);

        // 토큰 유효성 검사
        if (!jwtTokenProvider.validateUserToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }

        // 로그인된 사용자 정보 조회
        CustomSellerDetails customSellerDetails = (CustomSellerDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SellerEntity seller = customSellerDetails.getSeller();

        // 판매자 모드로 전환
        sellerService.switchToSellerMode(seller);

        // 판매자용 토큰 발급
        String sellerToken = jwtTokenProvider.generateSellerToken(seller.getEmail());

        StoreEntity store = storeRepository.findByStoreId(seller.getStoreId())
            .orElseThrow(() -> new IllegalArgumentException("사용자의 매장을 찾을 수 없습니다."));


        // 로그인된 사용자 정보와 전환된 셀러 정보에 접근 가능한 처리 수행
        String message = "로그인된 사용자: " + seller.getEmail() + "\n"
            + "스토어 이름: " + store.getStoreName() + "\n"
            + "판매자용 토큰: " + sellerToken;

        return ResponseEntity.ok().body(message);
    }

    // TODO : test 필요, 유저모드로 전환시 판매자용 토큰 비동기 방식으로 만료 처리
    // 유저 모드로 다시 전환
    @GetMapping("/info")
    public ResponseEntity<?> isUserModeButtonVisible() {
        // 사용자 정보 조회
        SellerEntity seller = sellerRepository.findByEmail("b244wer3w@naver.com")
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 원래 유저 페이지였을 경우
        if (seller.getSellerModeButton() != SellerModeButton.BUTTON_CLICK_TO_USER_MODE) {
            return ResponseEntity.badRequest().body("현재 페이지는 유저페이지입니다.");
        }

        sellerService.switchToUserMode(seller);

        return ResponseEntity.ok().body("유저 페이지로 접속했습니다.");
    }


    // TODO : 테스트 필요 글 작성
    // 소식 쓰기
    @PostMapping("/news/add") // seller
    public ResponseEntity<?> writeNews(@RequestBody NewsRequest newsRequest,
        @RequestHeader("Authorization") String authorizationHeader) {

        // 토큰 검증 및 유저 정보 확인
        if (!jwtTokenProvider.validateToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }

        // 토큰에서 이메일 추출
        String email = jwtTokenProvider.getEmailFromToken(
            jwtTokenProvider.getExtractToken(authorizationHeader));

        // 사용자 정보 조회
        SellerEntity seller = (SellerEntity) sellerDetailsService.loadUserByUsername(email);
        StoreEntity store = storeRepository.findByStoreId(seller.getStoreId())
            .orElseThrow(() -> new IllegalArgumentException("사용자의 매장을 찾을 수 없습니다."));

        // 소식 쓰기 로직 수행
        NewsDto news = sellerService.addNews(
            newsRequest.getTitle(),
            newsRequest.getContent()
        );

        return ResponseEntity.ok().body("소식을 성공적으로 작성했습니다." + NewsResponse.fromDTO(news));
    }

}