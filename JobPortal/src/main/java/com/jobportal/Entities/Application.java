package com.jobportal.Entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "application",uniqueConstraints = { @UniqueConstraint(columnNames = {"candidate_id", "job_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long applicationId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "candidate_id", nullable = false)
	private CandidateProfile candidate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_id", nullable = false)
	private Job job;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ApplicationStatus status;

	@Column(length = 2000)
	private String recruiterRemarks;

	private LocalDateTime appliedAt;

	private LocalDateTime updatedAt;

	@PrePersist
	public void prePersist() {

		appliedAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();

		if(status == null) {
			status = ApplicationStatus.APPLIED;
		}
	}

	@PreUpdate
	public void preUpdate() {
		updatedAt = LocalDateTime.now();
	}
}