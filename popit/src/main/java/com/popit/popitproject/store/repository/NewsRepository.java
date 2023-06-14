package com.popit.popitproject.store.repository;


import com.popit.popitproject.store.entity.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {

}
