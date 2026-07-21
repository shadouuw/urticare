package com.amine.urticare.service.impl;

import com.amine.urticare.model.entities.PatientProfile;
import com.amine.urticare.model.entities.RecoveryTransaction;
import com.amine.urticare.repository.PatientProfileRepository;
import com.amine.urticare.repository.RecoveryTransactionRepository;
import com.amine.urticare.resources.dto.*;
import com.amine.urticare.service.IBilanService;
import com.amine.urticare.service.IRecoveryService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class RecoveryServiceImpl implements IRecoveryService {

    private static final int OTP_TTL_SECONDS = 300;
    private static final int MAX_ATTEMPTS = 5;

    private final RecoveryTransactionRepository recoveryTransactionRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final IBilanService bilanService;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public RecoveryServiceImpl(
            RecoveryTransactionRepository recoveryTransactionRepository,
            PatientProfileRepository patientProfileRepository,
            IBilanService bilanService,
            PasswordEncoder passwordEncoder,
            JavaMailSender mailSender) {

        this.recoveryTransactionRepository = recoveryTransactionRepository;
        this.patientProfileRepository = patientProfileRepository;
        this.bilanService = bilanService;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Override
    public RecoveryStartResponse startRecovery(RecoveryRequest request) {
        PatientProfile profile = patientProfileRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        String otp = generateOtp();

        RecoveryTransaction txn = recoveryTransactionRepository.save(
                RecoveryTransaction.builder()
                        .id(UUID.randomUUID().toString())
                        .email(profile.getEmail())
                        .otpHash(passwordEncoder.encode(otp))
                        .expiresAt(Instant.now().plusSeconds(OTP_TTL_SECONDS))
                        .attempts(0)
                        .used(false)
                        .build()
        );

        sendOtp(profile.getEmail(), otp);

        RecoveryStartResponse response = new RecoveryStartResponse();
        response.setTransactionId(txn.getId());
        return response;
    }

    @Override
    public RecoveryResponse confirmRecovery(RecoveryConfirmRequest request) {
        RecoveryTransaction txn = recoveryTransactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new BadCredentialsException("Invalid recovery transaction"));

        if (txn.isUsed() || Instant.now().isAfter(txn.getExpiresAt())) {
            throw new BadCredentialsException("Recovery code expired");
        }

        if (txn.getAttempts() >= MAX_ATTEMPTS) {
            throw new BadCredentialsException("Recovery attempts exceeded");
        }

        if (!passwordEncoder.matches(request.getOtp(), txn.getOtpHash())) {
            txn.setAttempts(txn.getAttempts() + 1);
            recoveryTransactionRepository.save(txn);
            throw new BadCredentialsException("Invalid recovery code");
        }

        txn.setUsed(true);
        recoveryTransactionRepository.save(txn);

        PatientProfile profile = patientProfileRepository.findByEmail(txn.getEmail())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        RecoveryResponse response = new RecoveryResponse();
        response.setProfile(toProfileResponse(profile));
        response.setBilans(bilanService.getHistory(profile.getEmail()));
        return response;
    }

    // --------------------------- PRIVATE METHODS ---------------------------

    private void sendOtp(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("UrtiCare data recovery code");
        message.setText("Your UrtiCare data recovery code is: " + otp);
        mailSender.send(message);
    }

    private String generateOtp() {
        return String.valueOf(100000 + new SecureRandom().nextInt(900000));
    }

    private PatientProfileResponse toProfileResponse(PatientProfile p) {
        PatientProfileResponse r = new PatientProfileResponse();
        r.setId(p.getId());
        r.setEmail(p.getEmail());
        r.setFirstname(p.getFirstname());
        r.setLastname(p.getLastname());
        r.setPhone(p.getPhone());
        r.setRegion(p.getRegion());
        r.setCreatedAt(p.getCreatedAt());
        r.setUpdatedAt(p.getUpdatedAt());
        return r;
    }
}
