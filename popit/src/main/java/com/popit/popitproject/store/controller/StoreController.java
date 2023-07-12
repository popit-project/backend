package com.popit.popitproject.store.controller;


import com.popit.popitproject.notification.entity.NotificationEntity;
import com.popit.popitproject.notification.repository.NotificationRepository;
import com.popit.popitproject.notification.service.NotificationService;
import com.popit.popitproject.store.entity.LikeEntity;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.model.StoreCountDTO;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class StoreController {
    private final StoreService storeService;
    private final StoreRepository storeRepository;
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

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
    @GetMapping("/searchAll/{storeType}")
    public List<StoreCountDTO> findMapType(@PathVariable StoreType storeType) {
        List<StoreCountDTO> mapResults = storeService.findMapType(storeType);
        if(mapResults == null){
            return (List<StoreCountDTO>) ResponseEntity.ok("타입이 맞지 않습니다.");
        }
        return ResponseEntity.ok(mapResults).getBody();
    }


    @ApiOperation(
            value = "사용자 주변5km 스토어 조회"
            , notes = "사용자의 위치정보를 가지고와 주변 5KM 내에 있는 스토어를 조회합니다.")
    @GetMapping("/searchAll/5km")
    public List<MapMapping> find5km(@RequestParam("userLat") Double userLat, @RequestParam("userLon") Double userLon){
        return storeService.findStoreWithin5km(userLat,userLon);
    }

    // 좋아요 기능
//    @PostMapping("/{storeId}/toggle-like")
//    public ResponseEntity<?> toggleLikeStore(@PathVariable Long storeId, @RequestHeader(name = "Authorization") String token) {
//        StoreEntity store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));
//
//        String userEmail = jwtTokenService.getEmailFromToken(token.substring(7));
//        UserEntity user = userRepository.findByEmail(userEmail);
//        LikeEntity like = likeRepository.findByUserAndStore(user, store).orElse(null);
//
//        if (like == null) {
//            like = new LikeEntity(user, store);
//            likeRepository.save(like);
//            return ResponseEntity.ok("좋아요 되었습니다.");
//        } else {
//            likeRepository.delete(like);
//            return ResponseEntity.ok("좋아요가 취소되었습니다.");
//        }
//    }

    // 좋아요 websocket 추가
    @PostMapping("/{storeId}/toggle-like")
    public ResponseEntity<?> toggleLikeStore(@PathVariable Long storeId, @RequestHeader(name = "Authorization") String token) {
        StoreEntity store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        String userEmail = jwtTokenService.getEmailFromToken(token.substring(7));
        UserEntity user = userRepository.findByEmail(userEmail);
        LikeEntity like = likeRepository.findByUserAndStore(user, store).orElse(null);

        if (like == null) {
            like = new LikeEntity(user, store);
            likeRepository.save(like);

            // WebSocket을 이용해 알림 전송
            NotificationEntity notification = new NotificationEntity();
            notification.setUser(user);
            notification.setMessage(store.getStoreName() + "에 좋아요를 눌렀습니다.");

            NotificationEntity savedNotification = notificationRepository.save(notification);
            notificationService.notifyUser(savedNotification);

            return ResponseEntity.ok("좋아요 되었습니다.");
        } else {
            likeRepository.delete(like);
            return ResponseEntity.ok("좋아요가 취소되었습니다.");
        }
    }

    @GetMapping("/likes")
    public ResponseEntity<?> getAllLikes() {
        List<LikeEntity> likes = likeRepository.findAll();

        List<Map<String, Object>> likesInfo = likes.stream().map(like -> {
            Map<String, Object> likeInfo = new HashMap<>();
            likeInfo.put("userId", like.getUser().getUserId());
            likeInfo.put("storeId", like.getStore().getId());
            return likeInfo;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(likesInfo);
    }
}