package com.amine.urticare.resources;

import com.amine.urticare.resources.dto.PatientProfileRequest;
import com.amine.urticare.resources.dto.PatientProfileResponse;
import com.amine.urticare.service.IPatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final IPatientService patientService;

    public PatientController(IPatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<PatientProfileResponse> upsert(@Valid @RequestBody PatientProfileRequest request) {
        PatientProfileResponse response = patientService.upsert(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
