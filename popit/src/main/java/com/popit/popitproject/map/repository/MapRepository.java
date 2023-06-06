package com.popit.popitproject.map.repository;

import com.popit.popitproject.map.entity.MapEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapRepository extends JpaRepository<MapEntity, String> {

}
