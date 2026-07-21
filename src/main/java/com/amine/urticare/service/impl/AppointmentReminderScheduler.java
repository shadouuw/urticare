package com.amine.urticare.service.impl;

import com.amine.urticare.model.entities.Appointment;
import com.amine.urticare.repository.AppointmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Daily job that emails patients a reminder for any doctor appointment
 * happening tomorrow, using the same JavaMailSender already configured for
 * MFA/recovery emails. Runs once a day — a missed run (e.g. server down at
 * 9am) simply catches up on the next run, since it only marks an
 * appointment as reminded once the email actually goes out.
 */
@Component
public class AppointmentReminderScheduler {

    private static final Logger log = LoggerFactory.getLogger(AppointmentReminderScheduler.class);
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm");

    private final AppointmentRepository appointmentRepository;
    private final JavaMailSender mailSender;

    public AppointmentReminderScheduler(AppointmentRepository appointmentRepository, JavaMailSender mailSender) {
        this.appointmentRepository = appointmentRepository;
        this.mailSender = mailSender;
    }

    // Every day at 09:00 server time.
    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void sendTomorrowReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDateTime start = tomorrow.atStartOfDay();
        LocalDateTime end = tomorrow.plusDays(1).atStartOfDay();

        List<Appointment> dueTomorrow = appointmentRepository.findByReminderSentFalseAndDateTimeBetween(start, end);

        for (Appointment appointment : dueTomorrow) {
            try {
                sendReminderEmail(appointment);
                appointment.setReminderSent(true);
                appointmentRepository.save(appointment);
            } catch (Exception e) {
                log.warn("Failed to send appointment reminder for id={}: {}", appointment.getId(), e.getMessage());
            }
        }
    }

    private void sendReminderEmail(Appointment appointment) {
        String doctor = (appointment.getDoctorName() == null || appointment.getDoctorName().isBlank())
                ? "votre médecin" : appointment.getDoctorName();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(appointment.getPatientEmail());
        message.setSubject("Rappel : rendez-vous médical demain");
        message.setText("""
                Bonjour,

                Ceci est un rappel pour votre rendez-vous avec %s le %s.

                %s

                — UrtiCare
                """.formatted(
                doctor,
                appointment.getDateTime().format(DISPLAY_FORMAT),
                (appointment.getNotes() == null || appointment.getNotes().isBlank())
                        ? "" : "Notes : " + appointment.getNotes()
        ));
        mailSender.send(message);
    }
}
