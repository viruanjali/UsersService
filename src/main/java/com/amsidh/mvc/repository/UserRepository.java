package com.amsidh.mvc.repository;

import com.amsidh.mvc.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserId(String userId);
    Optional<UserEntity> findByEmailId(String emailId);
}
