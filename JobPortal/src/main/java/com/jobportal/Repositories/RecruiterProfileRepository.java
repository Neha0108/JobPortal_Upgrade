package com.jobportal.Repositories;

import com.jobportal.Entities.RecruiterProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, Long> {
    Optional<RecruiterProfile> findByUserUserId(Long userId);
}
