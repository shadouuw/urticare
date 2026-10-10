package com.amine.urticare.service.impl;

import com.amine.urticare.service.IReportEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

// Temporary: emails the generated monthly PDF report to a recipient chosen
// by the app at the user's request, for review during development. Remove
// this file (IReportEmailService, ReportEmailController, and the frontend
// call in my_bilans_screen.dart / report_email_service.dart) once no longer
// needed.
@Service
public class ReportEmailServiceImpl implements IReportEmailService {

    private final JavaMailSender mailSender;

    public ReportEmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendMonthlyReport(MultipartFile file, String recipientEmail, String patientEmail, String month) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(recipientEmail);

            StringBuilder subject = new StringBuilder("Urticapp - Rapport mensuel");
            if (month != null && !month.isBlank()) subject.append(" (").append(month).append(")");
            if (patientEmail != null && !patientEmail.isBlank()) subject.append(" - ").append(patientEmail);
            helper.setSubject(subject.toString());

            String bodyPatient = (patientEmail != null && !patientEmail.isBlank()) ? " pour " + patientEmail : "";
            helper.setText("Ci-joint le rapport mensuel généré depuis l'application Urticapp" + bodyPatient + ".");

            String filename = file.getOriginalFilename() != null && !file.getOriginalFilename().isBlank()
                    ? file.getOriginalFilename()
                    : "rapport.pdf";
            helper.addAttachment(filename, file);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new IllegalStateException("Unable to send the report by email", e);
        }
    }
}
