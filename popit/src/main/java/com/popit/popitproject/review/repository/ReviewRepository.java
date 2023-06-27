package com.popit.popitproject.review.repository;

import com.popit.popitproject.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity,Long> {
    List<ReviewEntity> findByStoreId(Long StoreId);
}
