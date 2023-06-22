package com.popit.popitproject.store.model;


import com.popit.popitproject.store.entity.NewsEntity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class NewsDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String storeName;

    public static NewsDto fromEntity(NewsEntity newsEntity) {
        return new NewsDto(
            newsEntity.getId(),
            newsEntity.getTitle(),
            newsEntity.getContent(),
            newsEntity.getCreatedAt(),
            newsEntity.getUpdatedAt(),
            newsEntity.getStore().getStoreName()
        );
    }
}
