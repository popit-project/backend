package com.popit.popitproject.news.model;

import com.popit.popitproject.news.entity.NotificationEntity;
import lombok.*;

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