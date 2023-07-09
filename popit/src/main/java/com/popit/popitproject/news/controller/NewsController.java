package com.popit.popitproject.news.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.popit.popitproject.common.ResponseDTO;
import com.popit.popitproject.news.entity.NewsEntity;
import com.popit.popitproject.news.model.NewsDTO;
import com.popit.popitproject.news.model.NewsListResponseDTO;
import com.popit.popitproject.news.model.NotificationDTO;
import com.popit.popitproject.news.service.NewsService;
import com.popit.popitproject.news.service.NotificationService;
import com.popit.popitproject.news.entity.NotificationEntity;
import com.popit.popitproject.store.entity.LikeEntity;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.repository.LikeRepository;
import com.popit.popitproject.store.repository.StoreSellerRepository;
import com.popit.popitproject.user.entity.UserEntity;
import io.jsonwebtoken.io.IOException;
import io.swagger.annotations.ApiOperation;

import com.popit.popitproject.news.repository.NotificationRepository;
import com.popit.popitproject.user.repository.UserRepository;
import com.popit.popitproject.user.service.JwtTokenService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NewsController {

    private final NewsService newsService;
    private final StoreSellerRepository sellerRepository;
    private final LikeRepository likeRepository;
    private final NotificationRepository notificationRepository;
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @ApiOperation(
        value = "소식 등록",
        notes = "소식 작성을 하고 저장/등록합니다."
    )
    @PostMapping(path = "/seller/news", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<NewsDTO>> createNews(
        @AuthenticationPrincipal String userId,
        @RequestPart("file") MultipartFile file,
        @RequestPart("dto") String dtoJson
    ) throws IOException {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            NewsDTO dto = objectMapper.readValue(dtoJson, NewsDTO.class);

            UserEntity user = newsService.getUserById(userId);
            StoreEntity store = sellerRepository.findById(user.getStore().getId())
                .orElseThrow();

            NewsEntity entity = newsService.convertToEntity(dto);
            entity.setStoreName(store.getStoreName());
            entity.setCity(store.getStoreAddress());
            entity.setCreateTime(LocalDateTime.now());
            entity.setSeller(user.getStore());

            List<NewsEntity> entities = newsService.createNews(file, dto, user, store);

            // 알림 생성 및 WebSocket을 통한 알림 전송
            List<LikeEntity> likes = likeRepository.findByStore(user.getStore());
            for (LikeEntity like : likes) {
                NotificationEntity notification = new NotificationEntity();
                notification.setUser(like.getUser());
                notification.setMessage(store.getStoreName() + "에서 새 글이 작성되었습니다.");

                NotificationEntity savedNotification = notificationRepository.save(notification);

                notificationService.notifyUser(savedNotification);
            }

            List<NewsDTO> dtos = entities.stream().map(NewsDTO::new).collect(Collectors.toList());
            ResponseDTO<NewsDTO> response = newsService.getResponseDTO(dtos);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return newsService.createErrorResponse(e.getMessage());
        }
    }

    @ApiOperation(
        value = "소식 목록",
        notes = "가게에 해당하는 소식글 리스트를 불러옵니다."
    )
    @GetMapping("/{storeName}/news")
    public ResponseEntity<ResponseDTO<NewsDTO>> retrieveNewsList(@PathVariable String storeName) {
        try {
            StoreEntity store = newsService.getStoreByName(storeName);
            List<NewsEntity> entities = newsService.retrieve(store.getId());
            List<NewsListResponseDTO> newsResponses = new ArrayList<>();

            for (NewsEntity entity : entities) {
                NewsListResponseDTO newsResponse = NewsListResponseDTO.toDTO(entity);
                newsResponses.add(newsResponse);
            }

            ResponseDTO<NewsDTO> response = newsService.getResponseListDTO(newsResponses);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return newsService.createErrorResponse(e.getMessage());
        }
    }

    @ApiOperation(
        value = "소식 삭제"
        , notes = "가게에 해당하는 목록 가져온 뒤 소식 id로 소식글을 삭제합니다.")
    @DeleteMapping("/seller/news/{newsId}")
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
    @PostMapping("/seller/notifications/count")
    public ResponseEntity<?> getNotificationCount(
            @RequestHeader(value = "Authorization") String token) {
        token = token.replace("Bearer ", "");
        String userId = jwtTokenService.getUserIdFromToken(token);
        UserEntity user = userRepository.findByUserId(userId);

        Long count = notificationRepository.countByUser(user);

        // WebSocket
        NotificationEntity notification = new NotificationEntity();
        notification.setUser(user);
        notification.setMessage("알림 수: " + count);
        notificationService.notifyUser(notification);

        return ResponseEntity.ok(count);
    }

    // 알림 List
    @GetMapping("/seller/notifications")
    public ResponseEntity<?> getUserNotifications(
        @RequestHeader(value = "Authorization") String token) {
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
    @DeleteMapping("/seller/notifications/{notificationId}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long notificationId) {
        notificationRepository.deleteById(notificationId);
        return ResponseEntity.ok().build();
    }

    // 알림 전체 삭제
    @ApiOperation(
            value = "알림 삭제"
    )
    @DeleteMapping("/seller/notifications")
    public ResponseEntity<?> deleteAllUserNotifications(
        @RequestHeader(value = "Authorization") String token) {
        token = token.replace("Bearer ", "");
        String userId = jwtTokenService.getUserIdFromToken(token);
        UserEntity user = userRepository.findByUserId(userId);

        List<NotificationEntity> notifications = notificationRepository.findByUser(user);
        for (NotificationEntity notification : notifications) {
            notificationRepository.delete(notification);
        }

        return ResponseEntity.ok().build();
    }
}
