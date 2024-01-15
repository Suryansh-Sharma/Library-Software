package com.suryansh.library.repository;

import com.suryansh.library.entity.RefreshTokenEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Integer> {
    Optional<RefreshTokenEntity> findByToken(String token);
    @Transactional
    @Modifying
    @Query("delete from RefreshTokenEntity e where e.id=:id")
    void deleteRefreshToken(int id);
}