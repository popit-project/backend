package com.popit.popitproject.news.service;

import com.popit.popitproject.news.entity.NotificationEntity;
import com.popit.popitproject.news.model.NotificationDTO;
import com.popit.popitproject.news.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final SimpMessagingTemplate template;
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(SimpMessagingTemplate template, NotificationRepository notificationRepository) {
        this.template = template;
        this.notificationRepository = notificationRepository;
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
}


//    private final SimpMessagingTemplate template;
//
//    @Autowired
//    public NotificationService(SimpMessagingTemplate template) {
//        this.template = template;
//    }
//
//    public void notifyUser(NotificationEntity notification) {
//        NotificationDTO notificationDTO = new NotificationDTO(notification);
//        this.template.convertAndSendToUser(notification.getUser().getUserId(), "/topic/notifications", notificationDTO);
//    }
//}
