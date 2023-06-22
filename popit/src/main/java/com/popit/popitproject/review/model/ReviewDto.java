package com.popit.popitproject.review.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect
public class ReviewDto {
    private Long id;
    private String comment;
    private String createDate;
    private String modifiedDate;
    private String email;
    private Long storeId;
}
