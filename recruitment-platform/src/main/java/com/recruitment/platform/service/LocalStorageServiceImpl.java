package com.recruitment.platform.service;

import com.recruitment.platform.common.exception.BadRequestException;
import com.recruitment.platform.common.exception.ResourceNotFoundException;
import com.recruitment.platform.common.exception.StorageException;
import com.recruitment.platform.config.properties.FileStorageProperties;
import com.recruitment.platform.dto.resume.StoredFile;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocalStorageServiceImpl implements StorageService {

    private final FileStorageProperties fileStorageProperties;

    @Override
    public StoredFile store(MultipartFile file, String subDirectory) {
        try {
            Path baseDir = resolveBaseDir();
            Path targetDir = baseDir.resolve(subDirectory).normalize();
            Files.createDirectories(targetDir);

            String extension = extensionOf(file.getOriginalFilename());
            String storedFileName = UUID.randomUUID() + (extension.isEmpty() ? "" : "." + extension);
            Path targetPath = targetDir.resolve(storedFileName).normalize();

            assertWithinBase(baseDir, targetPath);

            file.transferTo(targetPath);

            return new StoredFile(baseDir.relativize(targetPath).toString(), storedFileName, file.getSize());
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Resource loadAsResource(String filePath) {
        try {
            Path baseDir = resolveBaseDir();
            Path target = baseDir.resolve(filePath).normalize();
            assertWithinBase(baseDir, target);

            Resource resource = new UrlResource(target.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("File not found or is not readable.");
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new StorageException("Failed to load file.", e);
        }
    }

    @Override
    public void delete(String filePath) {
        try {
            Path baseDir = resolveBaseDir();
            Path target = baseDir.resolve(filePath).normalize();
            assertWithinBase(baseDir, target);
            Files.deleteIfExists(target);
        } catch (IOException e) {
            throw new StorageException("Failed to delete file.", e);
        }
    }

    private Path resolveBaseDir() {
        Path baseDir = Paths.get(fileStorageProperties.local().basePath()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(baseDir);
        } catch (IOException e) {
            throw new StorageException("Failed to initialize storage directory.", e);
        }
        return baseDir;
    }

    // Defends against path traversal (e.g. a crafted "../../etc/passwd"-style
    // subDirectory or filename) - the resolved path must never escape baseDir.
    private void assertWithinBase(Path baseDir, Path target) {
        if (!target.startsWith(baseDir)) {
            throw new BadRequestException("Invalid file path.");
        }
    }

    private String extensionOf(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}