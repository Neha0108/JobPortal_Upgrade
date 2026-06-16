package com.jobportal.Repositories;

import com.jobportal.Entities.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Long> {
}
