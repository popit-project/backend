package com.popit.popitproject.review.controller;

import com.popit.popitproject.review.model.CreateReviewRequest;
import com.popit.popitproject.review.model.ReviewDto;
import com.popit.popitproject.review.repository.ReviewRepository;
import com.popit.popitproject.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    @PostMapping("/write/{storeId}")
    public ResponseEntity<String> createComment(@PathVariable("storeId") Long storeId, @RequestBody CreateReviewRequest createReviewRequest) {
        reviewService.createComment(storeId,createReviewRequest);

        return ResponseEntity.ok("review write success!");
    }

    @GetMapping("/read/{storeId}/comment")
    public List<ReviewDto> getReviewsByStoreId(@PathVariable Long storeId) {
        return reviewService.getReviewByStoreId(storeId);
    }

    @GetMapping("/count/{storeId}")
    public Long getReviewCount(@PathVariable Long storeId){
        return (long) reviewService.getReviewCount(storeId);
    }



}
