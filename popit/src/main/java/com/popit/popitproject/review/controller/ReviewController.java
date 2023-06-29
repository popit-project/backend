package com.popit.popitproject.review.controller;

import com.popit.popitproject.review.model.ReviewDto;
import com.popit.popitproject.review.model.ReviewReadDto;
import com.popit.popitproject.review.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;

    @ApiOperation(
            value = "리뷰 작성"
            , notes = "스토어 ID로 들어가 리뷰 내용과 Email를 저장합니다.")
    @PostMapping("/write/{storeId}")
    public ResponseEntity<String> createComment(@PathVariable("storeId") Long storeId, @AuthenticationPrincipal String email,@RequestBody ReviewDto reviewDto) {
        reviewService.createComment(storeId,email,reviewDto);
        return ResponseEntity.ok("review write success!");
    }

    @ApiOperation(
            value = "스토어 리뷰 조회"
            , notes = "스토어 ID에 맞는 리뷰와 를 모두 불러옵니다.")
    @GetMapping("/read/{storeId}/comment")
    public List<ReviewReadDto> getReviewsByStoreId(@PathVariable Long storeId) {
        return reviewService.getReviewByStoreId(storeId);
    }

    @ApiOperation(
            value = "스토어 리뷰 개수"
            , notes = "스토어 ID 값을 가지고와 작성된 리뷰 개수를 추출합니다.")
    @GetMapping("/count/{storeId}")
    public Long getReviewCount(@PathVariable Long storeId){
        return (long) reviewService.getReviewCount(storeId);
    }

    @ApiOperation(
            value = "리뷰 삭제"
            , notes = "리뷰 ID를 입력해 그 리뷰를 email이 일치하는 사용자만 삭제합니다. ")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable("id") Long id, @AuthenticationPrincipal String email) {
        reviewService.deleteReview(id, email);
        return ResponseEntity.ok("Review delete !!");
    }

    @ApiOperation(
            value = "리뷰 수정"
            , notes = "리뷰 ID를 입력해 그 리뷰를 email이 일치하는 사용자만 수정합니다.")
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateReview(@PathVariable("id") Long id, @AuthenticationPrincipal String email,@RequestBody ReviewDto reviewDto) {
        reviewService.updateReview(id, email,reviewDto);
        return ResponseEntity.ok("Review update !!!");
    }

}
