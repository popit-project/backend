package com.popit.popitproject.store.controller;

import com.popit.popitproject.store.entity.NewsEntity;
import com.popit.popitproject.store.model.NewsDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewsResponse {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String storeName;


    public static NewsResponse fromDTO(NewsDto newsDto) {
        return new NewsResponse(
            newsDto.getId(),
            newsDto.getTitle(),
            newsDto.getContent(),
            newsDto.getCreatedAt(),
            newsDto.getUpdatedAt(),
            newsDto.getStoreName()
        );
    }
}
