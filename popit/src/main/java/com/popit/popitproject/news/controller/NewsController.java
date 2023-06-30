package com.popit.popitproject.news.controller;

import com.popit.popitproject.common.ResponseDTO;
import com.popit.popitproject.news.entity.NewsEntity;
import com.popit.popitproject.news.model.NewsDTO;
import com.popit.popitproject.news.model.NotificationDTO;
import com.popit.popitproject.news.service.NewsService;
import com.popit.popitproject.news.service.NotificationService;
import com.popit.popitproject.store.entity.LikeEntity;
import com.popit.popitproject.news.entity.NotificationEntity;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.repository.LikeRepository;
import com.popit.popitproject.store.repository.StoreSellerRepository;
import com.popit.popitproject.user.entity.UserEntity;
import java.time.LocalDateTime;

import com.popit.popitproject.news.repository.NotificationRepository;
import com.popit.popitproject.user.repository.UserRepository;
import com.popit.popitproject.user.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller")
public class NewsController {

    private final NewsService newsService;
    private final StoreSellerRepository sellerRepository;
    private final LikeRepository likeRepository;
    private final NotificationRepository notificationRepository;
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

//    @PostMapping("/news")
//    public ResponseEntity<ResponseDTO<NewsDTO>> createNews(
//        @AuthenticationPrincipal String userId, @RequestBody NewsDTO dto) {
//        try {
//            UserEntity user = newsService.getUserById(userId);
//            NewsEntity entity = newsService.convertToEntity(dto);
//
//            StoreEntity store = sellerRepository.findById(user.getStore().getId())
//                .orElseThrow();
//
//            entity.setStoreName(store.getStoreName());
//            entity.setCity(store.getStoreAddress()); // 동만 나오게
//            entity.setCreateTime(LocalDateTime.now());
//            entity.setSeller(user.getStore());
//
//            List<NewsEntity> entities = newsService.create(entity);
//            List<NewsDTO> dtos = entities.stream().map(NewsDTO::new).collect(Collectors.toList());
//
//            ResponseDTO<NewsDTO> response = newsService.getResponseDTO(
//                dtos);
//
//            return ResponseEntity.ok().body(response);
//        } catch (Exception e) {
//            return newsService.createErrorResponse(e.getMessage());
//        }
//    }

    @PostMapping("/news")
    public ResponseEntity<ResponseDTO<NewsDTO>> createNews(
            @AuthenticationPrincipal String userId, @RequestBody NewsDTO dto) {
        try {
            UserEntity user = newsService.getUserById(userId);
            NewsEntity entity = newsService.convertToEntity(dto);

            StoreEntity store = sellerRepository.findById(user.getStore().getId())
                    .orElseThrow();

            entity.setStoreName(store.getStoreName());
            entity.setCity(store.getStoreAddress()); // 동만 나오게
            entity.setCreateTime(LocalDateTime.now());
            entity.setSeller(user.getStore());

            List<NewsEntity> entities = newsService.create(entity);

            List<LikeEntity> likes = likeRepository.findByStore(user.getStore());

            // 알림 생성 및 WebSocket을 통한 알림 전송
            for (LikeEntity like : likes) {
                NotificationEntity notification = new NotificationEntity();
                notification.setUser(like.getUser());
                notification.setMessage(store.getStoreName() + "에서 새 글이 작성되었습니다.");

                NotificationEntity savedNotification = notificationRepository.save(notification);

                notificationService.notifyUser(savedNotification);
            }

            List<NewsDTO> dtos = entities.stream().map(NewsDTO::new).collect(Collectors.toList());

            ResponseDTO<NewsDTO> response = newsService.getResponseDTO(
                    dtos);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return newsService.createErrorResponse(e.getMessage());
        }
    }

    // (원본)
//    @PostMapping("/news")
//    public ResponseEntity<ResponseDTO<NewsDTO>> createNews(
//            @AuthenticationPrincipal String userId, @RequestBody NewsDTO dto) {
//        try {
//            UserEntity user = newsService.getUserById(userId);
//            NewsEntity entity = newsService.convertToEntity(dto);
//
//            StoreEntity store = sellerRepository.findById(user.getStore().getId())
//                    .orElseThrow();
//
//            entity.setStoreName(store.getStoreName());
//            entity.setCity(store.getStoreAddress()); // 동만 나오게
//            entity.setCreateTime(LocalDateTime.now());
//            entity.setSeller(user.getStore());
//
//            List<NewsEntity> entities = newsService.create(entity);
//
//            List<LikeEntity> likes = likeRepository.findByStore(user.getStore());
//
//            // 알림 생성
//            for (LikeEntity like : likes) {
//                NotificationEntity notification = new NotificationEntity();
//                notification.setUser(like.getUser());
//                notification.setMessage(store.getStoreName() + "에서 새 글이 작성되었습니다.");
//
//                notificationRepository.save(notification);
//            }
//
//            List<NewsDTO> dtos = entities.stream().map(NewsDTO::new).collect(Collectors.toList());
//
//            ResponseDTO<NewsDTO> response = newsService.getResponseDTO(
//                    dtos);
//
//            return ResponseEntity.ok().body(response);
//        } catch (Exception e) {
//            return newsService.createErrorResponse(e.getMessage());
//        }
//    }

    // Todo : 스토어에 해당하는 목록 가져오기, 내려주는 객체 당 목록 값 바꾸기
    @GetMapping("/{storeName}/news")
    public ResponseEntity<ResponseDTO<NewsDTO>> retrieveNewsList(@PathVariable String storeName) {

        try {
            StoreEntity store = newsService.getStoreByName(storeName);
            List<NewsEntity> entities = newsService.retrieve(store.getId());

            List<NewsDTO> dtos = entities.stream().map(NewsDTO::new).collect(Collectors.toList());

            ResponseDTO<NewsDTO> response = newsService.getResponseDTO(
                dtos);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return newsService.createErrorResponse(e.getMessage());
        }
    }

    // Todo : 스토어에 해당하는 목록 가져오기, id로  가져온 news 삭제
    @DeleteMapping("/{storeName}/news/{newsId}")
    public ResponseEntity<ResponseDTO<NewsDTO>> deleteNews(
        @AuthenticationPrincipal String userId, @PathVariable Long newsId) {


        try {
            UserEntity user = newsService.getUserById(userId);
            StoreEntity seller = newsService.getSellerByUser(user);
            NewsEntity entity = newsService.retrieveById(newsId);

            entity.setSeller(seller);

            List<NewsEntity> entities = newsService.delete(entity);
            List<NewsDTO> dtos = entities.stream().map(NewsDTO::new).collect(Collectors.toList());

            ResponseDTO<NewsDTO> response = newsService.getResponseDTO(
                dtos);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return newsService.createErrorResponse(e.getMessage());
        }
    }

    // 알림 Count
    @GetMapping("/notifications/count")
    public ResponseEntity<?> getNotificationCount(@RequestHeader(value="Authorization") String token) {
        token = token.replace("Bearer ", "");
        String userId = jwtTokenService.getUserIdFromToken(token);
        UserEntity user = userRepository.findByUserId(userId);

        Long count = notificationRepository.countByUser(user);

        return ResponseEntity.ok(count);
    }

    // 알림 List
    @GetMapping("/notifications")
    public ResponseEntity<?> getUserNotifications(@RequestHeader(value="Authorization") String token) {
        token = token.replace("Bearer ", "");
        String userId = jwtTokenService.getUserIdFromToken(token);
        UserEntity user = userRepository.findByUserId(userId);

        List<NotificationEntity> notifications = notificationRepository.findByUser(user);
        List<NotificationDTO> notificationDTOS = notifications.stream()
                .map(NotificationDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(notificationDTOS);
    }

    // 알림 삭제
    @DeleteMapping("/notifications/{notificationId}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long notificationId) {
        notificationRepository.deleteById(notificationId);
        return ResponseEntity.ok().build();
    }

    // 알림 전체 삭제
    @DeleteMapping("/notifications")
    public ResponseEntity<?> deleteAllUserNotifications(@RequestHeader(value="Authorization") String token) {
        token = token.replace("Bearer ", "");
        String userId = jwtTokenService.getUserIdFromToken(token);
        UserEntity user = userRepository.findByUserId(userId);

        List<NotificationEntity> notifications = notificationRepository.findByUser(user);
        for(NotificationEntity notification : notifications) {
            notificationRepository.delete(notification);
        }

        return ResponseEntity.ok().build();
    }
}
