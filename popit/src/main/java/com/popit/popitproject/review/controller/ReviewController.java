package com.popit.popitproject.review.controller;

import com.popit.popitproject.review.model.CreateReviewRequest;
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
    @PostMapping("/write/{storeName}")
    public ResponseEntity<String> createComment(@PathVariable String storeName, @RequestBody CreateReviewRequest createReviewRequest) {
        createReviewRequest.setStoreName(storeName);
        reviewService.createComment(createReviewRequest);

        return ResponseEntity.ok("review write success!");
    }

    @GetMapping("/read/{storeName}/comment")
    public List<Object[]> getReviewByStoreName(@PathVariable String storeName){
        return reviewService.getReviewByStoreName(storeName);
    }

    @GetMapping("/count/{storeName}")
    public int getReviewCount(@PathVariable String storeName){
        return reviewService.getReviewCount(storeName);
    }



}
