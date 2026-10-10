package com.amine.urticare.service;

import org.springframework.web.multipart.MultipartFile;

public interface IReportEmailService {

    void sendMonthlyReport(MultipartFile file, String recipientEmail, String patientEmail, String month);
}
