package com.popit.popitproject.news.repository;


import com.popit.popitproject.news.entity.NewsEntity;
import com.popit.popitproject.store.entity.StoreEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {

    Optional<List<NewsEntity>> findBySeller(StoreEntity seller);
}