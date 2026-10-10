package com.amine.urticare.resources;

import com.amine.urticare.model.entities.GouvernoratEntity;
import com.amine.urticare.repository.GouvernoratRepository;
import com.amine.urticare.resources.dto.ReferentialItemResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Public reference catalog (see ResourceServerConfig permitAll), not
// user-scoped — no @AuthenticationPrincipal needed.
@RestController
@RequestMapping("/api/gouvernorats")
public class GouvernoratController {

    private final GouvernoratRepository gouvernoratRepository;

    public GouvernoratController(GouvernoratRepository gouvernoratRepository) {
        this.gouvernoratRepository = gouvernoratRepository;
    }

    @GetMapping
    public List<ReferentialItemResponse> getAll() {
        return gouvernoratRepository.findAllByOrderBySortOrderAsc().stream()
                .map(g -> new ReferentialItemResponse(g.getId(), g.getName()))
                .toList();
    }
}
