package com.jobportal.Repositories;

import com.jobportal.Entities.RefreshToken;
import com.jobportal.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(Users user);
    void deleteByUser(Users user);
}
