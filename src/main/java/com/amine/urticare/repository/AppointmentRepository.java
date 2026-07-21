package com.amine.urticare.repository;

import com.amine.urticare.model.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientEmailOrderByDateTimeAsc(String patientEmail);

    List<Appointment> findByReminderSentFalseAndDateTimeBetween(LocalDateTime start, LocalDateTime end);
}
