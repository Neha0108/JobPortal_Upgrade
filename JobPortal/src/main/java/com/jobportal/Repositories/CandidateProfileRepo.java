package com.jobportal.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobportal.Entities.CandidateProfile;

@Repository
public interface CandidateProfileRepo extends JpaRepository<CandidateProfile, Long> {

    Optional<CandidateProfile> findByUser_UserId(Long userId);

    boolean existsByUser_UserId(Long userId);
}