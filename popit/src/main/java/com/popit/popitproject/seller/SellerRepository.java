package com.popit.popitproject.seller;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<SellerEntity, Long> {

    Optional<SellerEntity> findByEmail(String email);

    Optional<SellerEntity> findByStoreId(String storeId);

    Optional<SellerEntity> findByToken(String token);
}
