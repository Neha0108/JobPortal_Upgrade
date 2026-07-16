package com.jobportal.DTO;

import com.jobportal.Entities.EmploymentType;
import com.jobportal.Entities.JobCategory;
import com.jobportal.Entities.WorkMode;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDTO {

    private Long jobId;
    private String companyName;
    private Set<String> skillNames;
    private LocalDateTime createdAt;
    private String jobTitle;
    private String jobDescription;
    private String jobLocation;
    private EmploymentType employmentType;
    private JobCategory category;
    private WorkMode workMode;
    private Integer experienceRequired;
    private Double minSalary;
    private Double maxSalary;
    private Integer vacancies;
    private String benefits;
    private Boolean active;
    private LocalDateTime expiryDate;
    private Set<Long> skillIds;
}