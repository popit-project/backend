package com.popit.popitproject.store.repository;

import com.popit.popitproject.store.entity.StoreEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

    Optional<StoreEntity> findByStoreId(String storeId);
}
