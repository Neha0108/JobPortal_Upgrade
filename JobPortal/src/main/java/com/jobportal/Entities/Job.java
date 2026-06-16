package com.jobportal.Entities;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "job")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long jobId;

	@Column(nullable = false)
	private String jobTitle;

	@Column(nullable = false, length = 5000)
	private String jobDescription;

	@Column(nullable = false)
	private String jobLocation;

	@Enumerated(EnumType.STRING)
	private EmploymentType employmentType;

	@Enumerated(EnumType.STRING)
	private JobCategory category;

	@Enumerated(EnumType.STRING)
	private WorkMode workMode;

	private Integer experienceRequired;

	private Double minSalary;

	private Double maxSalary;

	private Integer vacancies;

	@Column(length = 2000)
	private String benefits;

	@Column(nullable = false)
	private Boolean active = true;

	private LocalDateTime expiryDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recruiter_id", nullable = false)
	private RecruiterProfile recruiter;

	@ManyToMany
	@JoinTable(
			name = "job_skills",
			joinColumns = @JoinColumn(name = "job_id"),
			inverseJoinColumns = @JoinColumn(name = "skill_id")
	)
	private Set<Skill> requiredSkills;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@PrePersist
	public void prePersist() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		updatedAt = LocalDateTime.now();
	}
}