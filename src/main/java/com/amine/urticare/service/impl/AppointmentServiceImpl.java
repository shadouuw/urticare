package com.amine.urticare.service.impl;

import com.amine.urticare.model.entities.Appointment;
import com.amine.urticare.repository.AppointmentRepository;
import com.amine.urticare.resources.dto.AppointmentRequest;
import com.amine.urticare.resources.dto.AppointmentResponse;
import com.amine.urticare.service.IAppointmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppointmentServiceImpl implements IAppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    @Transactional
    public AppointmentResponse create(AppointmentRequest request) {
        Appointment appointment = new Appointment();
        appointment.setPatientEmail(request.getPatientEmail());
        appointment.setDateTime(request.getDateTime());
        appointment.setDoctorName(request.getDoctorName());
        appointment.setNotes(request.getNotes());
        appointment.setReminderSent(false);

        Appointment saved = appointmentRepository.save(appointment);
        return toResponse(saved);
    }

    @Override
    public List<AppointmentResponse> getUpcoming(String patientEmail) {
        return appointmentRepository.findByPatientEmailOrderByDateTimeAsc(patientEmail)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private AppointmentResponse toResponse(Appointment a) {
        AppointmentResponse r = new AppointmentResponse();
        r.setId(a.getId());
        r.setPatientEmail(a.getPatientEmail());
        r.setDateTime(a.getDateTime());
        r.setDoctorName(a.getDoctorName());
        r.setNotes(a.getNotes());
        r.setReminderSent(a.getReminderSent());
        r.setCreatedAt(a.getCreatedAt());
        return r;
    }
}
