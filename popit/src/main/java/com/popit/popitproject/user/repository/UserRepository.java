package com.popit.popitproject.user.repository;

import com.popit.popitproject.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByUserId(String userId);
    UserEntity findByEmail(String email);
}