package com.popit.popitproject.review.model;

import com.popit.popitproject.review.entity.ReviewEntity;
import com.popit.popitproject.store.entity.StoreEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ReviewResponseDto {
    private Long id;
    private String comment;
    private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private String modifiedDate  = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private String email;
    private Long store;

//    public ReviewResponseDto(ReviewEntity review,StoreEntity store){
//        this.id = review.getId();
//        this.comment = review.getComment();
//        this.createdDate = review.getCreateDate();
//        this.modifiedDate = review.getModifiedDate();
////        this.nickname = store.get().getNickname();
////        this.store = store.getPost().getId();
//
//    }

}
