package com.amine.urticare.resources;

import com.amine.urticare.resources.dto.SuiviRequest;
import com.amine.urticare.resources.dto.SuiviResponse;
import com.amine.urticare.service.ISuiviService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suivis")
public class SuiviController {

    private final ISuiviService suiviService;

    public SuiviController(ISuiviService suiviService) {
        this.suiviService = suiviService;
    }

    @PostMapping
    public ResponseEntity<SuiviResponse> create(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody SuiviRequest request) {
        String userId = jwt.getSubject();
        SuiviResponse response = suiviService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/active")
    public ResponseEntity<SuiviResponse> getActive(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        SuiviResponse active = suiviService.getActive(userId);
        if (active == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(active);
    }

    @GetMapping("/has-active")
    public ResponseEntity<Boolean> hasActive(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(suiviService.hasActive(jwt.getSubject()));
    }

    @GetMapping("/history")
    public List<SuiviResponse> getHistory(@AuthenticationPrincipal Jwt jwt) {
        return suiviService.getHistory(jwt.getSubject());
    }

    @GetMapping("/{id}")
    public SuiviResponse getById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id) {
        return suiviService.getById(jwt.getSubject(), id);
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<Void> close(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id) {
        suiviService.close(jwt.getSubject(), id);
        return ResponseEntity.ok().build();
    }
}