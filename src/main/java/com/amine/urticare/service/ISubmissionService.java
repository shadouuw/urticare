
package com.amine.urticare.service;

import com.amine.urticare.resources.dto.SubmissionRequest;
import com.amine.urticare.resources.dto.SubmissionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ISubmissionService {

    SubmissionResponse create(String userId, SubmissionRequest request, List<MultipartFile> photos);

    Page<SubmissionResponse> getByUser(String userId, Pageable pageable);

    SubmissionResponse getById(String userId, Long id);

    void delete(String userId, Long id);
}
