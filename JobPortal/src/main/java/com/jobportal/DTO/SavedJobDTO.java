package com.jobportal.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedJobDTO {

    private Long savedJobId;
    private Long jobId;
    private String jobTitle;
    private String jobLocation;
    private LocalDateTime savedAt;
}