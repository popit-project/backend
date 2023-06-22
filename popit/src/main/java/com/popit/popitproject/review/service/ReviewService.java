package com.popit.popitproject.review.service;

import com.popit.popitproject.review.entity.ReviewEntity;
import com.popit.popitproject.review.exception.ReviewException;
import com.popit.popitproject.review.model.CreateReviewRequest;
import com.popit.popitproject.review.repository.ReviewRepository;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.repository.MapMapping;
import com.popit.popitproject.store.repository.StoreRepository;
import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    @Transactional
    public void createComment(CreateReviewRequest createReviewRequest){
        String email = createReviewRequest.getEmail();
        String comment = createReviewRequest.getComment();
        String storeName = createReviewRequest.getStoreName();

        UserEntity user = userRepository.findByEmail(email);
        if(user == null){
            throw new ReviewException("No valid user information.");
        }
        MapMapping store = storeRepository.findByStoreName(storeName);
        if(store == null){
            throw new ReviewException("No valid store information.");
        }

        ReviewEntity review = new ReviewEntity();
        review.setComment(comment);
        review.setEmail(user);
        review.setStore((StoreEntity.from(store)));

        reviewRepository.save(review);
    }
    @Transactional
    public List<Object[]> getReviewByStoreName(String storeName){
        List<Object[]> review =reviewRepository.findCommentsAndUserIdsByStoreName(storeName);
        return review;
    }

}
