package com.jobportal.Repositories;

import com.jobportal.Entities.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
}
