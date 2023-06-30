package com.popit.popitproject.news.repository;


import com.popit.popitproject.news.entity.NewsEntity;
import com.popit.popitproject.store.entity.StoreEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {

    Optional<List<NewsEntity>> findBySeller(StoreEntity seller);

    @Query("SELECT COUNT(n) FROM store s JOIN s.news n WHERE s.id = :storeId")
    int countNewsByStoreId(@Param("storeId") Long storeId);
    void deleteBySeller(StoreEntity store);

}