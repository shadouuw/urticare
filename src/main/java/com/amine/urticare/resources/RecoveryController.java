package com.amine.urticare.resources;

import com.amine.urticare.resources.dto.RecoveryConfirmRequest;
import com.amine.urticare.resources.dto.RecoveryRequest;
import com.amine.urticare.resources.dto.RecoveryResponse;
import com.amine.urticare.resources.dto.RecoveryStartResponse;
import com.amine.urticare.service.IRecoveryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recovery")
public class RecoveryController {

    private final IRecoveryService recoveryService;

    public RecoveryController(IRecoveryService recoveryService) {
        this.recoveryService = recoveryService;
    }

    @PostMapping("/request")
    public ResponseEntity<RecoveryStartResponse> requestRecovery(@Valid @RequestBody RecoveryRequest request) {
        return ResponseEntity.ok(recoveryService.startRecovery(request));
    }

    @PostMapping("/confirm")
    public ResponseEntity<RecoveryResponse> confirmRecovery(@Valid @RequestBody RecoveryConfirmRequest request) {
        return ResponseEntity.ok(recoveryService.confirmRecovery(request));
    }
}
