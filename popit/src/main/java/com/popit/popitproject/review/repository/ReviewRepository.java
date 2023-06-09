package com.popit.popitproject.review.repository;

import com.popit.popitproject.review.entity.ReviewEntity;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity,Long> {
    List<ReviewEntity> findByStoreId(Long StoreId);

    void deleteByStore(StoreEntity store);

    boolean existsByStoreAndEmail(StoreEntity store, UserEntity user);
}
