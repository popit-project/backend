package com.popit.popitproject.review.service;

import com.popit.popitproject.review.entity.ReviewEntity;
import com.popit.popitproject.review.exception.ReviewException;
import com.popit.popitproject.review.model.ReviewDto;
import com.popit.popitproject.review.model.ReviewReadDto;
import com.popit.popitproject.review.repository.ReviewRepository;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.repository.StoreRepository;
import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void createComment(Long storeId, String email, ReviewDto reviewDto){
        String comment = reviewDto.getComment();

        UserEntity user = userRepository.findByUserId(email);
        Optional<StoreEntity> optionalStore = storeRepository.findById(storeId);
        if (optionalStore.isPresent()) {
            StoreEntity store = optionalStore.get();
            ReviewEntity review = new ReviewEntity();

            boolean hasExistingReview = reviewRepository.existsByStoreAndEmail(store, user);
            if (hasExistingReview) {
                throw new ReviewException("이미 작성한 리뷰가 있습니다.");
            }

            review.setComment(comment);
            review.setEmail(user);
            review.setStore(store);
            review.setStoreName(store.getStoreName());



            reviewRepository.save(review);
        } else {
            throw new ReviewException("맞는 스토어가 없습니다.");
        }


    }
    @Transactional
    public List<ReviewReadDto> getReviewByStoreId(Long storeId) {
        List<ReviewEntity> reviewEntities = reviewRepository.findByStoreId(storeId);

        List<ReviewReadDto> reviewDtos = new ArrayList<>();
        for(ReviewEntity reviewEntity : reviewEntities){
            ReviewReadDto reviewDto = new ReviewReadDto();
            reviewDto.setId(reviewEntity.getId());
            reviewDto.setComment(reviewEntity.getComment());
            reviewDto.setEmail(String.valueOf(reviewEntity.getEmail().getNickname()));

            reviewDtos.add(reviewDto);
        }
        return reviewDtos;
    }
    @Transactional
    public int getReviewCount(Long storeId){
        List<ReviewEntity> review =reviewRepository.findByStoreId(storeId);
        int count = 0;

        for(int i = 0 ; i< review.size();i++){
            count+=1;
        }

        return count;
    }

    @Transactional
    public void deleteReview(Long id, String email){
        Optional<ReviewEntity> optionalReview = reviewRepository.findById(id);
        if (optionalReview.isPresent()) {
            ReviewEntity review = optionalReview.get();

            if (!review.getEmail().getUserId().equals(email)) {
                throw new ReviewException("삭제할 권한이 없습니다.");
            }

            reviewRepository.delete(review);
        } else {
            throw new ReviewException("리뷰를 찾을수 없습니다.");
        }
    }

    @Transactional
    public void updateReview(Long id, String email, ReviewDto reviewDto) {


        Optional<ReviewEntity> optionalReview = reviewRepository.findById(id);
        if (optionalReview.isPresent()) {
            ReviewEntity review = optionalReview.get();

            if (!review.getEmail().getUserId().equals(email)) {
                throw new ReviewException("수정할 권한이 없습니다.");
            }

            review.setComment(reviewDto.getComment());

            reviewRepository.save(review);
        } else {
            throw new ReviewException("리뷰를 찾을수 없습니다.");
        }
    }



}
