package com.popit.popitproject.map.repository;

import com.popit.popitproject.map.entity.MapEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MapRepository extends JpaRepository<MapEntity, Long> {
    Optional<MapEntity> findByStoreName(String storeName);

}
