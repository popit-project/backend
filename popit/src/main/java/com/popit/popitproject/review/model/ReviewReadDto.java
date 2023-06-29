package com.popit.popitproject.review.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewReadDto {
    private Long id;
    private String comment;
    private String email;
}
