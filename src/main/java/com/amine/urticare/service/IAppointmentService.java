package com.amine.urticare.service;

import com.amine.urticare.resources.dto.AppointmentRequest;
import com.amine.urticare.resources.dto.AppointmentResponse;

import java.util.List;

public interface IAppointmentService {

    AppointmentResponse create(AppointmentRequest request);

    List<AppointmentResponse> getUpcoming(String patientEmail);
}
