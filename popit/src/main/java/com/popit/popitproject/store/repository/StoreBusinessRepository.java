package com.popit.popitproject.store.repository;

import com.popit.popitproject.store.entity.StoreBusinessEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreBusinessRepository extends JpaRepository<StoreBusinessEntity, Long> {

}
