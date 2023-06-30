package com.popit.popitproject.news.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.popit.popitproject.news.entity.NewsEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewsDTO {
    private Long id;
    private Integer newsNumber;
    private String storeName;
    private String city;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;
    private String newsImgURL;
    private String content;

    public NewsDTO(final NewsEntity entity) {
        this.id = entity.getId();
        this.newsNumber = entity.getNewsNumber();
        this.createTime = entity.getCreateTime();
        this.storeName = entity.getStoreName();
        this.city = entity.getCity();
        this.newsImgURL = entity.getNewsImgURL();
        this.content = entity.getContent();
    }

    public static NewsEntity toEntity(final NewsDTO dto) {
        return NewsEntity.builder()
            .id(dto.getId())
            .newsNumber(dto.getNewsNumber())
            .city(dto.getCity())
            .newsImgURL(dto.getNewsImgURL())
            .content(dto.getContent())
            .newsImgURL(dto.getNewsImgURL())
            .build();
    }

}