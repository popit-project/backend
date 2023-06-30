package com.popit.popitproject.news.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.popit.popitproject.news.entity.NewsEntity;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewsListResponseDTO {
    private Long id;
    private Integer newsNumber;
    private String storeName;
    private String city;
    private String timeAgo;
    private String newsImgURL;
    private String content;

    public static NewsListResponseDTO toDTO(NewsEntity entity) {
        // NewsEntity에서 필요한 데이터를 가져와서 NewsListResponseDTO로 변환하는 로직 작성
        String timeAgo = formatTimeAgo(entity.getCreateTime());

        return NewsListResponseDTO.builder()
            .id(entity.getId())
            .newsNumber(entity.getNewsNumber())
            .storeName(entity.getStoreName())
            .city(entity.getCity())
            .timeAgo(timeAgo)
            .newsImgURL(entity.getNewsImgURL())
            .content(entity.getContent())
            .build();
    }

    private static String formatTimeAgo(LocalDateTime createTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Duration duration = Duration.between(createTime, currentDateTime);
        long minutes = duration.toMinutes();

        if (minutes < 1) {
            return "방금 전";
        } else if (minutes < 60) {
            return minutes + "분 전";
        } else if (minutes < 24 * 60) {
            long hours = minutes / 60;
            return hours + "시간 전";
        } else if (minutes < 30 * 24 * 60) {
            long days = minutes / (24 * 60);
            return days + "일 전";
        } else if (minutes < 12 * 30 * 24 * 60) {
            long months = minutes / (30 * 24 * 60);
            return months + "달 전";
        } else {
            long years = minutes / (12 * 30 * 24 * 60);
            return years + "년 전";
        }
    }
}