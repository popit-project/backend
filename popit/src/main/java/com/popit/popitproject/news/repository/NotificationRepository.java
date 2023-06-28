package com.popit.popitproject.news.repository;

import com.popit.popitproject.news.entity.NotificationEntity;
import com.popit.popitproject.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByUser(UserEntity user);
    Long countByUser(UserEntity user);
}