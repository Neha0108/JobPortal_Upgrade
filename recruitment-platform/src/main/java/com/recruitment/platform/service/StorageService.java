package com.recruitment.platform.service;

import com.recruitment.platform.dto.resume.StoredFile;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Abstraction over "where files physically live" - LocalStorageServiceImpl
 * today, swappable for an S3StorageServiceImpl later without touching
 * ResumeService at all (Strategy pattern, as planned in Step 1).
 */
public interface StorageService {

    StoredFile store(MultipartFile file, String subDirectory);

    Resource loadAsResource(String filePath);

    void delete(String filePath);
}