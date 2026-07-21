package com.amine.urticare.resources;

import com.amine.urticare.resources.dto.BilanRequest;
import com.amine.urticare.resources.dto.BilanResponse;
import com.amine.urticare.service.IBilanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bilans")
public class BilanController {

    private final IBilanService bilanService;

    public BilanController(IBilanService bilanService) {
        this.bilanService = bilanService;
    }

    @PostMapping
    public ResponseEntity<BilanResponse> upsert(@Valid @RequestBody BilanRequest request) {
        BilanResponse response = bilanService.upsert(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<BilanResponse> getHistory(@RequestParam("email") String email) {
        return bilanService.getHistory(email);
    }
}
