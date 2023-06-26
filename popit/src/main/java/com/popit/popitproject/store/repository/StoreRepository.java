package com.popit.popitproject.store.repository;

import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.model.StoreType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
    MapMapping findByStoreName(String storeName);
    List<MapMapping> findAllBy();
    List<MapMapping> findByStoreType(StoreType storeType);
}
