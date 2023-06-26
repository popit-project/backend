package com.popit.popitproject.review.repository;

import com.popit.popitproject.review.entity.ReviewEntity;
import com.popit.popitproject.review.model.CreateReviewRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity,Long> {
    List<ReviewEntity> findByStoreId(Long StoreId);
}
