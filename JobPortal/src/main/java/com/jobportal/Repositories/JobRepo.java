package com.jobportal.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobportal.Entities.Job;
import com.jobportal.Entities.Users;

@Repository
public interface JobRepo extends JpaRepository<Job,Long> {

    List<Job> findByRecruiterRecruiterId(Long recruiterId);

    List<Job> findByActiveTrue();
}
