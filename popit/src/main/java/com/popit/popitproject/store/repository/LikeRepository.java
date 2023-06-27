package com.popit.popitproject.store.repository;

import com.popit.popitproject.store.entity.LikeEntity;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByUserAndStore(UserEntity user, StoreEntity store);
    List<LikeEntity> findByStore(StoreEntity store);
    Long countByStore(StoreEntity store);
}
