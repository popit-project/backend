package com.popit.popitproject.review.service;

import com.popit.popitproject.review.entity.ReviewEntity;
import com.popit.popitproject.review.exception.ReviewException;
import com.popit.popitproject.review.model.CreateReviewRequest;
import com.popit.popitproject.review.model.ReviewDto;
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
//    @Transactional
//    public void createComment(CreateReviewRequest createReviewRequest){
//        String email = createReviewRequest.getEmail();
//        String comment = createReviewRequest.getComment();
//        Long StoreId = createReviewRequest.getStoreId();
//        String storeName = createReviewRequest.getStoreName();
//
//        UserEntity user = userRepository.findByEmail(email);
//        if(user == null){
//            throw new ReviewException("No valid user information.");
//        }
//        Optional<StoreEntity> optionalStore = storeRepository.findById(StoreId);
//        if (optionalStore.isPresent()) {
//            StoreEntity store = optionalStore.get();
//            ReviewEntity review = new ReviewEntity();
//            review.setComment(comment);
//            review.setEmail(user);
//            review.setStore(store);
//            review.setStoreName(store.getStoreName());
//            reviewRepository.save(review);
//        } else {
//            throw new ReviewException("No valid store information.");
//        }
//    }
    @Transactional
    public void createComment(Long storeId, CreateReviewRequest createReviewRequest){

        String email = createReviewRequest.getEmail();
        String comment = createReviewRequest.getComment();

        UserEntity user = userRepository.findByEmail(email);
        if(user == null){
            throw new ReviewException("No valid user information.");
        }
        Optional<StoreEntity> optionalStore = storeRepository.findById(storeId);
        if (optionalStore.isPresent()) {
            StoreEntity store = optionalStore.get();
            ReviewEntity review = new ReviewEntity();
            review.setComment(comment);
            review.setEmail(user);
            review.setStore(store);
            review.setStoreName(store.getStoreName());
            reviewRepository.save(review);
        } else {
            throw new ReviewException("No valid store information.");
        }
    }
    @Transactional
    public List<ReviewDto> getReviewByStoreId(Long storeId) {
        List<ReviewEntity> reviewEntities = reviewRepository.findByStoreId(storeId);
        List<ReviewDto> reviewDtos = new ArrayList<>();
        for(ReviewEntity reviewEntity : reviewEntities){
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setComment(reviewEntity.getComment());
            reviewDto.setEmail(reviewEntity.getEmail().getEmail());
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


}
