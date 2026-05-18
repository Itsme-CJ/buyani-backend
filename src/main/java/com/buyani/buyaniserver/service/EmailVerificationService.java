package com.buyani.buyaniserver.service;

import com.buyani.buyaniserver.entity.User;
import com.buyani.buyaniserver.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final VerificationService verificationService;
    private final EmailService emailService;
    private final UserRepo userRepo;

    public void sendVerificationCode(String email) {
        // 1. Find the user
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found with email: " + email));

        // 2. Generate and save the 6-digit code
        String code = verificationService.generateEmailVerificationCode(user.getUserId());

        // 3. Send the email
        try {
            emailService.sendEmailVerification(
                user.getEmail(),
                user.getFirstName(),
                code
            );
            log.info("Verification code sent to {}", email);
        } catch (MessagingException e) {
            log.error("Failed to send verification email to {}: {}", email, e.getMessage());
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send email");
        }
    }

    public boolean verifyCode(String email, String code) {
        try {
            verificationService.verifyEmailCode(email, code);
            return true;
        } catch (Exception e) {
            log.warn("Verification failed for {}: {}", email, e.getMessage());
            return false;
        }
    }
}