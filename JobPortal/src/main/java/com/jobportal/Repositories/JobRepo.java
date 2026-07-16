package com.jobportal.Repositories;

import java.util.List;

import com.jobportal.DTO.JobDTO;
import com.jobportal.Entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface JobRepo extends JpaRepository<Job,Long> {

    List<Job> findByRecruiterRecruiterId(Long recruiterId);

    List<Job> findByActiveTrue();
        @Query("""
        SELECT j FROM Job j
        WHERE
        (:keyword IS NULL OR
            LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(j.jobDescription) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND
        (:location IS NULL OR LOWER(j.jobLocation) = LOWER(:location))
        AND
        (:category IS NULL OR j.category = :category)
        AND
        (:workMode IS NULL OR j.workMode = :workMode)
        AND
        (:employmentType IS NULL OR j.employmentType = :employmentType)
        AND j.active = true""")
        List<Job> searchJobs(
                @Param("keyword") String keyword,
                @Param("location") String location,
                @Param("category") JobCategory category,
                @Param("workMode") WorkMode workMode,
                @Param("employmentType") EmploymentType employmentType
        );
    }
