
package com.amine.urticare.service.impl;

import com.amine.urticare.model.entities.Submission;
import com.amine.urticare.model.entities.SubmissionPhoto;
import com.amine.urticare.repository.SubmissionRepository;
import com.amine.urticare.resources.dto.SubmissionRequest;
import com.amine.urticare.resources.dto.SubmissionResponse;
import com.amine.urticare.service.ISubmissionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class SubmissionServiceImpl implements ISubmissionService {

    private final SubmissionRepository submissionRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public SubmissionServiceImpl(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    @Override
    @Transactional
    public SubmissionResponse create(String userId, SubmissionRequest request, List<MultipartFile> photos) {
        Submission submission = new Submission();
        submission.setUserId(userId);
        submission.setPillCount(request.getPillCount());
        submission.setFrequency(request.getFrequency());
        submission.setItchLevel(request.getItchLevel());
        submission.setSeverity(request.getSeverity());
        submission.setAffectedAreas(request.getAffectedAreas());
        submission.setNotes(request.getNotes());

        // Handle photo uploads
        if (photos != null && !photos.isEmpty()) {
            for (MultipartFile file : photos) {
                if (!file.isEmpty()) {
                    String savedUrl = saveFile(file, userId);
                    SubmissionPhoto photo = new SubmissionPhoto();
                    photo.setFilename(file.getOriginalFilename());
                    photo.setUrl(savedUrl);
                    photo.setContentType(file.getContentType());
                    photo.setFileSize(file.getSize());
                    photo.setSubmission(submission);
                    submission.getPhotos().add(photo);
                }
            }
        }

        Submission saved = submissionRepository.save(submission);
        return toResponse(saved);
    }

    @Override
    public Page<SubmissionResponse> getByUser(String userId, Pageable pageable) {
        return submissionRepository
                .findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(this::toResponse);
    }

    @Override
    public SubmissionResponse getById(String userId, Long id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        if (!submission.getUserId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        return toResponse(submission);
    }

    @Override
    @Transactional
    public void delete(String userId, Long id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        if (!submission.getUserId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        submissionRepository.delete(submission);
    }

    // ---- Private helpers ----

    private String saveFile(MultipartFile file, String userId) {
        try {
            Path uploadPath = Paths.get(uploadDir, userId);
            Files.createDirectories(uploadPath);

            String ext = getExtension(file.getOriginalFilename());
            String filename = UUID.randomUUID() + ext;
            Path filePath = uploadPath.resolve(filename);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return baseUrl + "/uploads/" + userId + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    private String getExtension(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot) : "";
    }

    private SubmissionResponse toResponse(Submission s) {
        SubmissionResponse r = new SubmissionResponse();
        r.setId(s.getId());
        r.setPillCount(s.getPillCount());
        r.setFrequency(s.getFrequency());
        r.setItchLevel(s.getItchLevel());
        r.setSeverity(s.getSeverity());
        r.setAffectedAreas(s.getAffectedAreas());
        r.setNotes(s.getNotes());
        r.setCreatedAt(s.getCreatedAt());
        r.setPhotoUrls(
            s.getPhotos().stream()
                .map(SubmissionPhoto::getUrl)
                .toList()
        );
        return r;
    }
}
