package com.popit.popitproject.notification.repository;

import com.popit.popitproject.notification.entity.NotificationEntity;
import com.popit.popitproject.user.entity.UserEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByUser(UserEntity user);
    Long countByUser(UserEntity user);
}