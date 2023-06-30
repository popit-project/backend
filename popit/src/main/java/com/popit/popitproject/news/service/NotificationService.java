package com.popit.popitproject.news.service;

import com.popit.popitproject.news.entity.NotificationEntity;
import com.popit.popitproject.news.model.NotificationDTO;
import com.popit.popitproject.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final SimpMessagingTemplate template;

    @Autowired
    public NotificationService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void notifyUser(NotificationEntity notification) {
        NotificationDTO notificationDTO = new NotificationDTO(notification);
        this.template.convertAndSendToUser(notification.getUser().getUserId(), "/topic/notifications", notificationDTO);
    }
}
