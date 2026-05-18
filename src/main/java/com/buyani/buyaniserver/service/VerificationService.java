package com.buyani.buyaniserver.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.buyani.buyaniserver.entity.User;
import com.buyani.buyaniserver.entity.Verification;
import com.buyani.buyaniserver.repository.UserRepo;
import com.buyani.buyaniserver.repository.VerificationRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VerificationService {
  private static final String NEW_USER      = "NEW";
  private static final String ACTIVE_CODE   = "ACT";
  private static final String INACTIVE_CODE = "INC";
  private static final String EMAIL_TYPE    = "EMAIL"; // NEW

  @Autowired
  private VerificationRepo verificationRepo;

  @Autowired
  private UserRepo userRepo;

  // Existing (unchanged)
  public String generateCode(Integer userId) {
    Verification verification                   = new Verification();
    LocalDateTime expiration                    = LocalDateTime.now().plusWeeks(1);
    Optional<Verification> optionalVerification = verificationRepo.findByUserId(userId);

    if (optionalVerification.isPresent()) {
      verification = optionalVerification.get();
    } else {
      verification.setUserId(userId);
      verification.setType(NEW_USER);
    }

    verification.setCode(generateUUID());
    verification.setStatus(ACTIVE_CODE);
    verification.setExpiration(convertLocalDateToLong(expiration));

    verificationRepo.save(verification);
    return verification.getCode();
  }

  // NEW: Generate 6-digit code for email verification
  public String generateEmailVerificationCode(Integer userId) {
    String numericCode = String.valueOf(100000 + new SecureRandom().nextInt(900000));
    LocalDateTime expiration = LocalDateTime.now().plusMinutes(15);

    Optional<Verification> optionalVerification = verificationRepo.findByUserId(userId);
    Verification verification = optionalVerification.orElseGet(Verification::new);

    if (optionalVerification.isEmpty()) {
      verification.setUserId(userId);
    }

    verification.setCode(numericCode);
    verification.setType(EMAIL_TYPE);
    verification.setStatus(ACTIVE_CODE);
    verification.setExpiration(convertLocalDateToLong(expiration));

    verificationRepo.save(verification);
    return numericCode;
  }

  // NEW: Validate the code submitted by the user
  public void verifyEmailCode(String email, String code) {
    User user = userRepo.findByEmail(email)
        .orElseThrow(() -> {
          log.error("User not found: {}", email);
          return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        });

    Verification verification = verificationRepo.findByUserId(user.getUserId())
        .orElseThrow(() -> {
          log.error("No verification record for userId: {}", user.getUserId());
          return new ResponseStatusException(HttpStatus.BAD_REQUEST, "No verification record found");
        });

    Long now = convertLocalDateToLong(LocalDateTime.now());

    if (verification.getStatus().equals(INACTIVE_CODE)) {
      log.error("Code already used for userId: {}", user.getUserId());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code already used");
    }

    if (now > verification.getExpiration()) {
      log.error("Code expired for userId: {}", user.getUserId());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code has expired");
    }

    if (!verification.getCode().equals(code)) {
      log.error("Wrong code for userId: {}", user.getUserId());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid code");
    }

    verification.setStatus(INACTIVE_CODE);
    verificationRepo.save(verification);

    user.setEmailVerified(true);
    userRepo.save(user);

    log.info("Email verified for userId: {}", user.getUserId());
  }

  // Existing (unchanged)
  public ResponseEntity<Object> checkNewPasswordVerification(String code, Boolean resendRequest) throws NotFoundException {
    Optional<Verification> optionalVerification = verificationRepo.findByCode(code);

    if (!optionalVerification.isPresent()) {
      log.error("Code doesn't exists");
      throw new NotFoundException();
    }

    Verification verification = optionalVerification.get();
    Long dateToday = convertLocalDateToLong(LocalDateTime.now());

    log.info("Resend Request : {}", resendRequest);
    log.info("Status : {}", verification.getStatus());

    User user = userRepo.findById(verification.getUserId()).orElse(null);
    if (user == null) {
      log.error("User doesn't exists");
      throw new NotFoundException();
    }

    if (resendRequest.equals(Boolean.FALSE) &&
        verification.getStatus().equals(INACTIVE_CODE) &&
        dateToday < verification.getExpiration()) {
      log.error("Inactive Code");
      throw new NotFoundException();
    }

    if (resendRequest.equals(Boolean.FALSE) && dateToday > verification.getExpiration()) {
      log.error("Expired code");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  private static String generateUUID() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }

  private static Long convertLocalDateToLong(LocalDateTime datetime) {
    return datetime.toEpochSecond(ZoneOffset.UTC);
  }
}
