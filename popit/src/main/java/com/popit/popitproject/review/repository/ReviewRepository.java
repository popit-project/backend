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
//    @Query("SELECT r.comment, u.email FROM ReviewEntity r JOIN r.store s JOIN r.email u WHERE s.storeName = :storeName")
//    List<Object[]> findCommentsAndUserIdsByStoreName(@Param("storeName") Long StoreId);

    List<ReviewEntity> findByStoreId(Long StoreId);
}
