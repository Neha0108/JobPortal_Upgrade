package com.jobportal.Repositories;

import com.jobportal.Entities.RefreshToken;
import com.jobportal.Entities.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(Users user);
    @Transactional
    @Modifying
    void deleteByUser(Users user);
}
