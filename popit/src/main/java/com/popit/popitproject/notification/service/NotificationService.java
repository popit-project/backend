package com.popit.popitproject.notification.service;

import com.popit.popitproject.notification.entity.NotificationEntity;
import com.popit.popitproject.notification.model.NotificationDTO;
import com.popit.popitproject.notification.repository.NotificationRepository;
import com.popit.popitproject.store.entity.LikeEntity;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.repository.LikeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final SimpMessagingTemplate template;
    private final NotificationRepository notificationRepository;
    private final LikeRepository likeRepository;

    @Autowired
    public NotificationService(SimpMessagingTemplate template, NotificationRepository notificationRepository,
        LikeRepository likeRepository) {
        this.template = template;
        this.notificationRepository = notificationRepository;
        this.likeRepository = likeRepository;
    }

    public void notifyUser(NotificationEntity newNotification) {
        NotificationDTO notificationDTO = new NotificationDTO(newNotification);
        this.template.convertAndSendToUser(newNotification.getUser().getUserId(), "/topic/notifications", notificationDTO);

        List<NotificationEntity> notifications = notificationRepository.findByUser(newNotification.getUser());
        List<NotificationDTO> notificationDTOS = notifications.stream()
                .map(NotificationDTO::new)
                .collect(Collectors.toList());
        this.template.convertAndSendToUser(newNotification.getUser().getUserId(), "/topic/notifications/list", notificationDTOS);
    }

    public void notifyUserCount(String userId, Long count) {
        System.out.println("userId = " + userId + " count = " + count);
        this.template.convertAndSendToUser(userId, "/topic/notifications/count", count);
    }


    public void newsNotification(StoreEntity store) {
        // 알림 생성 및 WebSocket을 통한 알림 전송
        List<LikeEntity> likes = likeRepository.findByStore(store);  // 해당 가게를 좋아요한 사용자만 조회
        for (LikeEntity like : likes) {
            NotificationEntity notification = new NotificationEntity();
            notification.setUser(like.getUser());
            notification.setMessage(store.getStoreName() + "에서 새 글이 작성되었습니다.");

            NotificationEntity savedNotification = notificationRepository.save(notification);

            notifyUser(savedNotification);

            // 해당 사용자의 새로운 알림 카운트를 가져온 후 WebSocket을 통해 전송
            Long count = notificationRepository.countByUser(like.getUser());
            notifyUserCount(like.getUser().getUserId(), count);
        }
    }

    // 오픈, 종료 하루 전 알림
    public void storeOpeningOrClosingNotification(StoreEntity store) {
        List<LikeEntity> likes = likeRepository.findByStore(store);
        for (LikeEntity like : likes) {
            NotificationEntity notification = new NotificationEntity();
            notification.setUser(like.getUser());
            String message;
            if (store.getOpenDate().isEqual(LocalDate.now().plusDays(1))) {
                message = store.getStoreName() + "가 내일 오픈합니다!";
            } else {
                message = store.getStoreName() + "가 내일 마감합니다!";
            }
            notification.setMessage(message);
            NotificationEntity savedNotification = notificationRepository.save(notification);
            notifyUser(savedNotification);
            Long count = notificationRepository.countByUser(like.getUser());
            notifyUserCount(like.getUser().getUserId(), count);
        }
    }
}