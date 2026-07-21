
package com.amine.urticare.resources;

import com.amine.urticare.resources.dto.PagedResponse;
import com.amine.urticare.resources.dto.SubmissionRequest;
import com.amine.urticare.resources.dto.SubmissionResponse;
import com.amine.urticare.service.ISubmissionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final ISubmissionService submissionService;

    public SubmissionController(ISubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping
    public ResponseEntity<SubmissionResponse> create(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam("pillCount") Integer pillCount,
            @RequestParam("frequency") String frequency,
            @RequestParam("itchLevel") Integer itchLevel,
            @RequestParam("severity") String severity,
            @RequestParam(value = "affectedAreas", required = false) List<String> affectedAreas,
            @RequestParam(value = "notes", required = false) String notes,
            @RequestParam(value = "photos", required = false) List<MultipartFile> photos) {

        String userId = jwt.getSubject();

        SubmissionRequest request = new SubmissionRequest();
        request.setPillCount(pillCount);
        request.setFrequency(frequency);
        request.setItchLevel(itchLevel);
        request.setSeverity(severity);
        request.setAffectedAreas(affectedAreas != null ? affectedAreas : List.of());
        request.setNotes(notes);

        SubmissionResponse response = submissionService.create(userId, request, photos);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public PagedResponse<SubmissionResponse> getAll(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        String userId = jwt.getSubject();
        Page<SubmissionResponse> result = submissionService.getByUser(userId,
                PageRequest.of(page, size, Sort.by("createdAt").descending()));

        return new PagedResponse<>(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isLast()
        );
    }

    @GetMapping("/{id}")
    public SubmissionResponse getById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id) {
        String userId = jwt.getSubject();
        return submissionService.getById(userId, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id) {
        String userId = jwt.getSubject();
        submissionService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}
