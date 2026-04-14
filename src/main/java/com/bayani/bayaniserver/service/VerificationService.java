package com.bayani.bayaniserver.service;

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

import com.bayani.bayaniserver.entity.User;
import com.bayani.bayaniserver.entity.Verification;
import com.bayani.bayaniserver.repository.UserRepo;
import com.bayani.bayaniserver.repository.VerificationRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VerificationService {
  private static final String NEW_USER = "NEW";
  private static final String ACTIVE_CODE = "ACT";
  private static final String INACTIVE_CODE = "INC";

  @Autowired
  private VerificationRepo verificationRepo;

  @Autowired
  private UserRepo userRepo;

  public String generateCode(Integer userId) {
    Verification verification                   = new Verification();
    LocalDateTime expiration                    = LocalDateTime.now().plusWeeks(1);
    Optional<Verification> optionalVerification = verificationRepo.findByUserId(userId);

    // Check if verification exists
    if (optionalVerification.isPresent()) {
      // Update verification
      verification = optionalVerification.get();
    } else {
      // Create verification
      verification.setUserId(userId);
      verification.setType(NEW_USER);
    }

    verification.setCode(generateUUID());
    verification.setStatus(ACTIVE_CODE);
    verification.setExpiration(convertLocalDateToLong(expiration));

    verificationRepo.save(verification);

    return verification.getCode();
  }  

  public ResponseEntity<Object> checkNewPasswordVerification(String code, Boolean resendRequest) throws NotFoundException {
    Optional<Verification> optionalVerification = verificationRepo.findByCode(code);

    // Check if Verification is not exists
    if (!optionalVerification.isPresent()) {
      log.error("Code doesn't exists");
      throw new NotFoundException();
    }

    Verification verification = optionalVerification.get();
    Long dateToday = convertLocalDateToLong(LocalDateTime.now());
    
    log.info("Resend Request : {}", resendRequest);
    log.info("Status : {}", verification.getStatus());

    User user = userRepo.findById(verification.getUserId()).orElse(null);
    // User doesn't exists
    if (user == null) {
      log.error("User doesn't exists");

      throw new NotFoundException();
    }

    // Inactive Code
    if (resendRequest.equals(Boolean.FALSE) &&
      verification.getStatus().equals(INACTIVE_CODE) && 
      dateToday < verification.getExpiration()) {

      log.error("Inactive Code");

      throw new NotFoundException();
    }

    // Expired verification
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
