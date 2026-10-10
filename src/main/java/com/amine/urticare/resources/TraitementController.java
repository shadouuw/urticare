package com.amine.urticare.resources;

import com.amine.urticare.resources.dto.TraitementRequest;
import com.amine.urticare.resources.dto.TraitementResponse;
import com.amine.urticare.service.ITraitementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/traitements")
public class TraitementController {

    private final ITraitementService traitementService;

    public TraitementController(ITraitementService traitementService) {
        this.traitementService = traitementService;
    }

    // Upserts by (userId, type) in the request body — a patient's oral and
    // injectable prescriptions are independent, so saving one never
    // overwrites the other.
    @PostMapping
    public ResponseEntity<TraitementResponse> upsert(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody TraitementRequest request) {
        String userId = jwt.getSubject();
        TraitementResponse response = traitementService.upsert(userId, request);
        return ResponseEntity.ok(response);
    }

    // Returns every active prescription (0, 1, or 2: at most one oral and
    // one injectable).
    @GetMapping
    public List<TraitementResponse> getAll(@AuthenticationPrincipal Jwt jwt) {
        return traitementService.getAll(jwt.getSubject());
    }

    // Cancels the prescription of the given type — bilans/suivi history are
    // untouched, only the "active prescription" itself is removed.
    @DeleteMapping("/{type}")
    public ResponseEntity<Void> cancel(@AuthenticationPrincipal Jwt jwt, @PathVariable String type) {
        traitementService.cancel(jwt.getSubject(), type);
        return ResponseEntity.noContent().build();
    }
}
