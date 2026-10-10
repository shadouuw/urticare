package com.amine.urticare.resources;

import com.amine.urticare.repository.HospitalRepository;
import com.amine.urticare.resources.dto.HospitalResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Public reference catalog (see ResourceServerConfig permitAll), not
// user-scoped — no @AuthenticationPrincipal needed. Optional ?type=
// filter ('public' | 'private') matches the profile's "suivi public ou
// privé" choice, so the app only shows relevant hospitals.
@RestController
@RequestMapping("/api/hospitals")
public class HospitalController {

    private final HospitalRepository hospitalRepository;

    public HospitalController(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    @GetMapping
    public List<HospitalResponse> getAll(@RequestParam(required = false) String type) {
        final var entities = (type == null || type.isBlank())
                ? hospitalRepository.findAllByOrderBySortOrderAsc()
                : hospitalRepository.findByTypeOrderBySortOrderAsc(type);
        return entities.stream()
                .map(h -> new HospitalResponse(h.getId(), h.getName(), h.getType()))
                .toList();
    }
}
