package com.buyani.buyaniserver.service;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.buyani.buyaniserver.dto.PasswordResetDTO;
import com.buyani.buyaniserver.dto.ResetPasswordRequest;
import com.buyani.buyaniserver.dto.SignUpRequest;
import com.buyani.buyaniserver.entity.User;
import com.buyani.buyaniserver.entity.Verification;
import com.buyani.buyaniserver.repository.UserRepo;
import com.buyani.buyaniserver.repository.VerificationRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthService {
  private static final String NEW_USER = "NEW";
  private static final String ACTIVE_CODE = "ACT";
  private static final String INACTIVE_CODE = "INC";

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private VerificationService verificationService;

  @Autowired
  private EmailService emailService;

  @Autowired
  private VerificationRepo verificationRepo;

  public User registerUser(SignUpRequest request) {
    if (Boolean.TRUE.equals(userRepo.existsByEmail(request.getEmail()))) {
      log.error("Registration failed: Username {} is already exist", request.getEmail());
      return null;
    }

    if (!request.getPassword().equals(request.getConfirmPassword())) {
      log.error("Registration failed: Passwords do not match");
      return null;
    }

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    User user = new User();
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());

    return userRepo.save(user);
  }

  public ResponseEntity<Object> forgotPassword(PasswordResetDTO requestBody)
      throws MessagingException, IOException {

    User user = userRepo.findByEmail(requestBody.getEmail()).orElse(null);
    if (user == null) {
      log.error("Email doesn't exist");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    log.info("Generating code for user {}", user.getUserId());

    Locale locale = Locale.getDefault();
    String code = verificationService.generateCode(user.getUserId());
    emailService.sendForgotPasswordEmail(user, locale, code);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  public ResponseEntity<Object> newPassword(ResetPasswordRequest resetPasswordRequest) {
    String code = resetPasswordRequest.getCode();

    Optional<Verification> optionalVerification = verificationRepo.findByCode(code);

    if (optionalVerification.isEmpty()) {
      log.error("Failed to create a new password: Invalid code!");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    Verification verification = optionalVerification.get();
    if (verification.getStatus().equals(INACTIVE_CODE)) {
      log.error("Failed to create a new password: Inactive code!");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    User user = userRepo.findById(verification.getUserId()).orElse(null);
    if (user == null) {
      log.error("Failed to create a new password: Invalid user code!");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    verification.setStatus(INACTIVE_CODE);
    String newPassword = resetPasswordRequest.getNewPassword();
    user.setPassword(passwordEncoder.encode(newPassword));

    userRepo.save(user);
    verificationRepo.save(verification);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  public ResponseEntity<Object> newPasswordCx(ResetPasswordRequest resetPasswordRequest) {
    if (resetPasswordRequest.getUserId() == null) {
      log.error("Failed to change password: userId is missing!");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId is required");
    }

    User user = userRepo.findByUserId(resetPasswordRequest.getUserId()).orElse(null);
    if (user == null) {
      log.error("Failed to change password: User not found!");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
    }

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    if (resetPasswordRequest.getCurrentPassword() == null ||
        !passwordEncoder.matches(resetPasswordRequest.getCurrentPassword(), user.getPassword())) {
      log.error("Failed to change password: Current password is incorrect!");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
    }

    String newPassword = resetPasswordRequest.getNewPassword();
    if (newPassword == null || newPassword.length() < 6) {
      log.error("Failed to change password: New password is too short!");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password must be at least 6 characters");
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepo.save(user);

    log.info("Password changed successfully for userId {}", user.getUserId());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  // ✅ NEW — for forgot password reset (no current password needed)
  public ResponseEntity<Object> resetPasswordCx(ResetPasswordRequest resetPasswordRequest) {
    if (resetPasswordRequest.getUserId() == null) {
      log.error("Failed to reset password: userId is missing!");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId is required");
    }

    User user = userRepo.findByUserId(resetPasswordRequest.getUserId()).orElse(null);
    if (user == null) {
      log.error("Failed to reset password: User not found!");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
    }

    String newPassword = resetPasswordRequest.getNewPassword();
    if (newPassword == null || newPassword.length() < 6) {
      log.error("Failed to reset password: New password is too short!");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password must be at least 6 characters");
    }

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepo.save(user);

    log.info("Password reset successfully for userId {}", user.getUserId());
    return new ResponseEntity<>(HttpStatus.OK);
  }
}