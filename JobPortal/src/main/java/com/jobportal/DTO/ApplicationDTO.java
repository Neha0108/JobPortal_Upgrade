package com.jobportal.DTO;

import java.time.LocalDateTime;

import com.jobportal.Entities.ApplicationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationDTO {

    private Long applicationId;
    private Long jobId;
    private String jobTitle;
    private Long candidateId;
    private String candidateName;
    private ApplicationStatus status;
    private String recruiterRemarks;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}