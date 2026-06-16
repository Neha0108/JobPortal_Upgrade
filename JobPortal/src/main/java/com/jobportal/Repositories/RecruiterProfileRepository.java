package com.jobportal.Repositories;

import com.jobportal.Entities.RecruiterProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, Long> {
}
