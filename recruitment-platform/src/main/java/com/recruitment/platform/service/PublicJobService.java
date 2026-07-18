package com.recruitment.platform.service;

import com.recruitment.platform.common.exception.ResourceNotFoundException;
import com.recruitment.platform.config.RedisConfig;
import com.recruitment.platform.dto.job.PublicJobMapper;
import com.recruitment.platform.dto.job.PublicJobResponse;
import com.recruitment.platform.entity.JobStatus;
import com.recruitment.platform.entity.JobType;
import com.recruitment.platform.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicJobService {

    private final JobRepository jobRepository;
    private final PublicJobMapper publicJobMapper;

    /**
     * Cached under CACHE_JOB_LISTINGS (5 min TTL, see RedisConfig) - job search
     * is the highest-traffic read in the whole system (every anonymous visitor
     * and every candidate homepage load hits it), so it's the first thing worth
     * taking off the DB.
     */
    @Cacheable(
            cacheNames = RedisConfig.CACHE_JOB_LISTINGS,
            key = "#keyword + '_' + #location + '_' + #jobType + '_' + #status + '_' + #pageable.pageNumber + '_' + #pageable.pageSize"
    )
    public Page<PublicJobResponse> search(String keyword, String location, JobType jobType, JobStatus status, Pageable pageable) {
        // Public browsing should only ever surface OPEN jobs by default -
        // a null status filter still gets pinned to OPEN here, not "any status",
        // so DRAFT/CLOSED postings never leak to anonymous visitors.
        JobStatus effectiveStatus = status != null ? status : JobStatus.OPEN;
        return jobRepository.search(keyword, location, jobType, effectiveStatus, pageable)
                .map(publicJobMapper::toResponse);
    }

    @Cacheable(cacheNames = RedisConfig.CACHE_JOB_DETAIL, key = "#jobId")
    public PublicJobResponse getById(UUID jobId) {
        return jobRepository.findByIdAndDeletedAtIsNull(jobId)
                .map(publicJobMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found."));
    }
}
