package com.popit.popitproject.notification.model;

import com.popit.popitproject.notification.entity.NotificationEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NotificationDTO {

    private Long id;
    private String message;

    public NotificationDTO(NotificationEntity entity) {
        this.id = entity.getId();
        this.message = entity.getMessage();
    }
}