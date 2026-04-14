package com.bayani.bayaniserver.service;

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

import com.bayani.bayaniserver.dto.PasswordResetDTO;
import com.bayani.bayaniserver.dto.ResetPasswordRequest;
import com.bayani.bayaniserver.dto.SignUpRequest;
import com.bayani.bayaniserver.entity.User;
import com.bayani.bayaniserver.entity.Verification;
import com.bayani.bayaniserver.repository.UserRepo;
import com.bayani.bayaniserver.repository.VerificationRepo;

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

    // Check if Verification does not exists
    if (optionalVerification.isEmpty()) {
      log.error("Failed to create a new password: Invalid code!");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    Verification verification   = optionalVerification.get();
    if (verification.getStatus().equals(INACTIVE_CODE)) {
        log.error("Failed to create a new password: Inactive code!");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    
    User user = userRepo.findById(verification.getUserId()).orElse(null);
    // Check if User is does not exists
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

    User user = userRepo.findByUserId(resetPasswordRequest.getUserId()).orElse(null);
  
    if (user == null) {
      log.error("Failed to create a new password: Invalid user!");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    String newPassword = resetPasswordRequest.getNewPassword();
    user.setPassword(passwordEncoder.encode(newPassword));

    userRepo.save(user);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
