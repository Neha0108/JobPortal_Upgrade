package com.recruitment.platform.repository;

import com.recruitment.platform.entity.RecruiterProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, UUID> {

    Optional<RecruiterProfile> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);

    long countByCompanyId(UUID companyId);
}
