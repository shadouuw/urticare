package com.amine.urticare.service;

import com.amine.urticare.model.entities.MfaTransaction;
import com.amine.urticare.model.entities.User;

/**
 * Service interface for handling Multi-Factor Authentication (MFA) operations.
 */
public interface IMfaService {

    /**
     * Checks whether MFA is currently required for authentication.
     *
     * @return {@code true} if MFA is enabled, {@code false} otherwise.
     */
    boolean isMfaRequired();

    /**
     * Creates a new MFA transaction for the given user.
     * Typically used to generate and send an OTP.
     *
     * @param user the user for whom the MFA transaction is created
     * @return the created {@link MfaTransaction} containing transaction details
     */
    MfaTransaction createTransaction(User user);

    /**
     * Validates the OTP provided by the user for a specific MFA transaction.
     *
     * @param transactionId the ID of the MFA transaction
     * @param otp the one-time password provided by the user
     * @return the authenticated {@link User} if the OTP is valid
     * @throws RuntimeException if the OTP is invalid or the transaction is expired
     */
    User validateOtp(String transactionId, String otp);

    /**
     * Resends the OTP for the specified MFA transaction.
     *
     * @param transactionId the ID of the MFA transaction
     */
    void resendOtp(String transactionId);
}
