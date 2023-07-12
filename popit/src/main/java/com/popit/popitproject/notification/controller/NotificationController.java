package com.popit.popitproject.notification.controller;

import com.popit.popitproject.news.service.NewsService;
import com.popit.popitproject.notification.entity.NotificationEntity;
import com.popit.popitproject.notification.model.NotificationDTO;
import com.popit.popitproject.notification.repository.NotificationRepository;
import com.popit.popitproject.notification.service.NotificationService;
import com.popit.popitproject.store.repository.StoreSellerRepository;
import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.repository.UserRepository;
import com.popit.popitproject.user.service.JwtTokenService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NotificationController {

    private final NewsService newsService;
    private final StoreSellerRepository sellerRepository;
    private final NotificationRepository notificationRepository;
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @ApiOperation(value = "알림 count")
    @PostMapping("/seller/notifications/count")
    public ResponseEntity<?> getNotificationCount(
        @RequestHeader(value = "Authorization") String token) {
        token = token.replace("Bearer ", "");
        String userId = jwtTokenService.getUserIdFromToken(token);
        UserEntity user = userRepository.findByUserId(userId);

        Long count = notificationRepository.countByUser(user);

        // WebSocket
        notificationService.notifyUserCount(userId, count);

        return ResponseEntity.ok(count);
    }

    @ApiOperation(value = "알림 list")
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

    @ApiOperation(value = "알림 삭제")
    @DeleteMapping("/seller/notifications/{notificationId}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long notificationId) {
        notificationRepository.deleteById(notificationId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "알림 전체 삭제")
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
