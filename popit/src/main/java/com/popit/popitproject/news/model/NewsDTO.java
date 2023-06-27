package com.popit.popitproject.news.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.popit.popitproject.news.entity.NewsEntity;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewsDTO {
    private Long id;
    private String storeName;
    private String city;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;
    private String image;
    private String content;

    public NewsDTO(final NewsEntity entity) {
        this.id = entity.getId();
        this.createTime = entity.getCreateTime();
        this.storeName = entity.getStoreName();
        this.city = entity.getCity();
        this.image = entity.getImage();
        this.content = entity.getContent();
    }

    public static NewsEntity toEntity(final NewsDTO dto) {
        return NewsEntity.builder()
            .id(dto.getId())
            .city(dto.getCity())
            .image(dto.getImage())
            .content(dto.getContent())
            .build();
    }

    public static String extractDongFromAddress(String address) {
        String pattern = "\\S+시\\s+\\S+구\\s+(\\S+동)";

        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(address);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

}