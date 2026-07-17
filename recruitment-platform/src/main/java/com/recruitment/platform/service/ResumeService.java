package com.recruitment.platform.service;

import com.recruitment.platform.common.exception.BadRequestException;
import com.recruitment.platform.common.exception.ResourceNotFoundException;
import com.recruitment.platform.config.properties.FileStorageProperties;
import com.recruitment.platform.dto.resume.DownloadableFile;
import com.recruitment.platform.dto.resume.ResumeMapper;
import com.recruitment.platform.dto.resume.ResumeResponse;
import com.recruitment.platform.dto.resume.StoredFile;
import com.recruitment.platform.entity.CandidateProfile;
import com.recruitment.platform.entity.Resume;
import com.recruitment.platform.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final CandidateProfileService candidateProfileService;
    private final StorageService storageService;
    private final FileStorageProperties fileStorageProperties;
    private final ResumeMapper resumeMapper;

    // Tika is thread-safe and stateless once constructed - one shared instance is fine.
    private static final Tika TIKA = new Tika();

    public ResumeResponse uploadResume(UUID userId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Uploaded file is empty.");
        }
        if (file.getSize() > fileStorageProperties.maxFileSizeBytes()) {
            long maxMb = fileStorageProperties.maxFileSizeBytes() / (1024 * 1024);
            throw new BadRequestException("File exceeds the maximum allowed size of " + maxMb + "MB.");
        }

        // Content-based detection, NOT the filename extension or client-supplied
        // Content-Type header - both are trivially spoofable.
        String detectedType;
        try {
            detectedType = TIKA.detect(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            throw new BadRequestException("Unable to read the uploaded file.");
        }
        if (!fileStorageProperties.allowedContentTypes().contains(detectedType)) {
            throw new BadRequestException("Only PDF and DOCX files are allowed.");
        }

        CandidateProfile profile = candidateProfileService.getProfileEntityByUserId(userId);
        StoredFile stored = storageService.store(file, profile.getId().toString());

        boolean isFirstResume = resumeRepository.findByCandidateProfileId(profile.getId()).isEmpty();

        Resume resume = Resume.builder()
                .candidateProfile(profile)
                .fileName(file.getOriginalFilename())
                .filePath(stored.filePath())
                .fileType(detectedType)
                .fileSize(stored.size())
                .primary(isFirstResume) // first upload is automatically the default resume
                .uploadedAt(Instant.now())
                .build();

        return resumeMapper.toResponse(resumeRepository.save(resume));
    }

    @Transactional(readOnly = true)
    public List<ResumeResponse> getMyResumes(UUID userId) {
        CandidateProfile profile = candidateProfileService.getProfileEntityByUserId(userId);
        return resumeRepository.findByCandidateProfileId(profile.getId()).stream()
                .map(resumeMapper::toResponse).toList();
    }

    public ResumeResponse setPrimaryResume(UUID userId, UUID resumeId) {
        Resume resume = getOwnedResume(userId, resumeId);
        resumeRepository.clearPrimaryFlagForCandidate(resume.getCandidateProfile().getId());
        resume.setPrimary(true);
        return resumeMapper.toResponse(resumeRepository.save(resume));
    }

    public void deleteResume(UUID userId, UUID resumeId) {
        Resume resume = getOwnedResume(userId, resumeId);
        storageService.delete(resume.getFilePath());
        resumeRepository.delete(resume);
    }

    @Transactional(readOnly = true)
    public DownloadableFile downloadResume(UUID userId, UUID resumeId) {
        Resume resume = getOwnedResume(userId, resumeId);
        return new DownloadableFile(storageService.loadAsResource(resume.getFilePath()), resume.getFileName(), resume.getFileType());
    }

    /** Package-private so ApplicationService (below) can validate a candidate-supplied resumeId too. */
    Resume getOwnedResume(UUID userId, UUID resumeId) {
        CandidateProfile profile = candidateProfileService.getProfileEntityByUserId(userId);
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found."));
        if (!resume.getCandidateProfile().getId().equals(profile.getId())) {
            throw new AccessDeniedException("You do not have permission to access this resume.");
        }
        return resume;
    }
}
