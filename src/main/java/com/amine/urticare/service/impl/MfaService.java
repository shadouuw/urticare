package com.amine.urticare.service.impl;

import com.amine.urticare.model.entities.MfaTransaction;
import com.amine.urticare.model.entities.User;
import com.amine.urticare.repository.MfaTransactionRepository;
import com.amine.urticare.repository.UserRepository;
import com.amine.urticare.service.IMfaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.amine.urticare.common.Constants;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

/**
 * Service implementation for Multi-Factor Authentication (MFA).
 * Handles OTP generation, validation, and sending via email or SMS.
 */
@Service
@Transactional
public class MfaService implements IMfaService {

    @Value("${auth.server.security.mfa.enabled}")
    private Boolean isMfaEnabled;

    @Value("${auth.server.security.mfa.otp-ttl-seconds}")
    private int mfaTtlSeconds;

    @Value("${auth.server.security.mfa.max-attempts}")
    private int maxAttempts;

    @Value("${auth.server.security.mfa.method}")
    private String method;

    private final MfaTransactionRepository repository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public MfaService(
            MfaTransactionRepository repository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JavaMailSender mailSender) {

        this.repository = repository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMfaRequired() {
        return isMfaEnabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MfaTransaction createTransaction(User user) {

        String otp = generateOtp();

        MfaTransaction txn = repository.save(
                MfaTransaction.builder()
                        .id(UUID.randomUUID().toString())
                        .userId(user.getId())
                        .otpHash(passwordEncoder.encode(otp))
                        .expiresAt(Instant.now().plusSeconds(mfaTtlSeconds))
                        .attempts(0)
                        .used(false)
                        .build()
        );

        sendOtp(user, otp);

        return txn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User validateOtp(String transactionId, String otp) {

        MfaTransaction txn = repository.findById(transactionId)
                .orElseThrow(() ->
                        new BadCredentialsException("Invalid MFA transaction"));

        if (txn.isUsed() || Instant.now().isAfter(txn.getExpiresAt())) {
            throw new BadCredentialsException("MFA code expired");
        }

        if (txn.getAttempts() >= maxAttempts) {
            throw new BadCredentialsException("MFA attempts exceeded");
        }

        if (!passwordEncoder.matches(otp, txn.getOtpHash())) {
            txn.setAttempts(txn.getAttempts() + 1);
            repository.save(txn);
            throw new BadCredentialsException("Invalid MFA code");
        }

        txn.setUsed(true);
        repository.save(txn);

        return userRepository.findById(txn.getUserId())
                .orElseThrow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resendOtp(String transactionId) {

        MfaTransaction txn = repository.findById(transactionId)
                .orElseThrow();

        User user = userRepository.findById(txn.getUserId())
                .orElseThrow();

        String otp = generateOtp();

        txn.setOtpHash(passwordEncoder.encode(otp));
        txn.setExpiresAt(Instant.now().plusSeconds(mfaTtlSeconds));
        txn.setAttempts(0);

        repository.save(txn);

        sendOtp(user, otp);
    }

    // --------------------------- PRIVATE METHODS ---------------------------

    /**
     * Sends the OTP to the user using the configured MFA method.
     *
     * @param user the target user
     * @param otp  the one-time password
     */
    private void sendOtp(User user, String otp) {
        switch (method) {
            case Constants.EMAIL, Constants.SMS -> sendEmail(user.getEmail(), otp);
            default -> throw new IllegalStateException("Unsupported MFA method: " + method);
        }
    }

    /**
     * Sends the OTP via email.
     *
     * @param email recipient email
     * @param otp   one-time password
     */
    private void sendEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your verification code");
        message.setText("Your MFA code is: " + otp);
        mailSender.send(message);
    }

    /**
     * Generates a random 6-digit OTP.
     *
     * @return the OTP as a string
     */
    private String generateOtp() {
        return String.valueOf(100000 + new SecureRandom().nextInt(900000));
    }
}
