package com.amine.urticare.resources;

import com.amine.urticare.resources.dto.AppointmentRequest;
import com.amine.urticare.resources.dto.AppointmentResponse;
import com.amine.urticare.service.IAppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final IAppointmentService appointmentService;

    public AppointmentController(IAppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@Valid @RequestBody AppointmentRequest request) {
        AppointmentResponse response = appointmentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<AppointmentResponse> getUpcoming(@RequestParam("email") String email) {
        return appointmentService.getUpcoming(email);
    }
}
