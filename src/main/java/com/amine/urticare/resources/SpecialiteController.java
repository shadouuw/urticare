package com.amine.urticare.resources;

import com.amine.urticare.repository.SpecialiteRepository;
import com.amine.urticare.resources.dto.ReferentialItemResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Public reference catalog (see ResourceServerConfig permitAll), not
// user-scoped — no @AuthenticationPrincipal needed.
@RestController
@RequestMapping("/api/specialites")
public class SpecialiteController {

    private final SpecialiteRepository specialiteRepository;

    public SpecialiteController(SpecialiteRepository specialiteRepository) {
        this.specialiteRepository = specialiteRepository;
    }

    @GetMapping
    public List<ReferentialItemResponse> getAll() {
        return specialiteRepository.findAllByOrderBySortOrderAsc().stream()
                .map(s -> new ReferentialItemResponse(s.getId(), s.getName()))
                .toList();
    }
}
