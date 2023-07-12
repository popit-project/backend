package com.popit.popitproject.store.repository;

import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.user.entity.UserEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreSellerRepository extends JpaRepository<StoreEntity, Long> {

    Optional<StoreEntity> findByUser(UserEntity user);

    boolean existsByStoreName(String storeName);

    Optional<StoreEntity> findByStoreName(String storeName);

    @Query(value = "SELECT * FROM store s WHERE s.close_date < CURDATE()", nativeQuery = true)
    List<StoreEntity> findExpiredStores();

}