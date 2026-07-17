package com.recruitment.platform.repository;

import com.recruitment.platform.entity.Job;
import com.recruitment.platform.entity.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {

    // Every query filters deletedAt IS NULL explicitly - soft-deleted jobs
    // must never surface through normal reads.

    Optional<Job> findByIdAndDeletedAtIsNull(UUID id);

    Page<Job> findByRecruiterProfileIdAndDeletedAtIsNull(UUID recruiterProfileId, Pageable pageable);

    Page<Job> findByStatusAndDeletedAtIsNull(JobStatus status, Pageable pageable);

    long countByRecruiterProfileIdAndDeletedAtIsNull(UUID recruiterProfileId);

    long countByRecruiterProfileIdAndStatusAndDeletedAtIsNull(UUID recruiterProfileId, JobStatus status);

    long countByStatusAndDeletedAtIsNull(JobStatus status);

    // Search/filter for the public job listing endpoint - keyword matches title
    // or description; location/type/status are optional (null = "don't filter").
    @Query("""
            SELECT j FROM Job j
            WHERE j.deletedAt IS NULL
              AND (:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                     OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:location IS NULL OR LOWER(j.location) = LOWER(:location))
              AND (:jobType IS NULL OR j.jobType = :jobType)
              AND (:status IS NULL OR j.status = :status)
            """)
    Page<Job> search(
            @Param("keyword") String keyword,
            @Param("location") String location,
            @Param("jobType") com.recruitment.platform.entity.JobType jobType,
            @Param("status") JobStatus status,
            Pageable pageable);
}
