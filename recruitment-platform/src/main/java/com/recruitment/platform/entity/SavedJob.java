package com.recruitment.platform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

// createdAt (inherited from BaseEntity) doubles as "saved_at" - no separate column needed.
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "saved_jobs",
        uniqueConstraints = @UniqueConstraint(name = "uq_candidate_saved_job", columnNames = {"candidate_profile_id", "job_id"}))
public class SavedJob extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidate_profile_id", nullable = false)
    private CandidateProfile candidateProfile;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
}