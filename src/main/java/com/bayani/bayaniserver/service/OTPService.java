package com.bayani.bayaniserver.service;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bayani.bayaniserver.dto.OtpDTO;
import com.bayani.bayaniserver.entity.Reservation;
import com.bayani.bayaniserver.entity.Store;
import com.bayani.bayaniserver.entity.Transaction;
import com.bayani.bayaniserver.entity.User;
import com.bayani.bayaniserver.repository.TransactionRepo;
import com.bayani.bayaniserver.repository.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OTPService {
  @Autowired
  UserRepo userRepo;

  @Autowired
  TransactionRepo transactionRepo;

  private static final String AUTHORIZATION = "Authorization";

  public OtpDTO requestForgotPasswordOTP(OtpDTO request) throws MessagingException, IOException {
    String number = request.getNumber();
    Integer code = getCode();
    Optional<User> isExist = userRepo.findByPhoneNumber(number);
    if (isExist.isEmpty()) {
      return null;
    }

    User user = isExist.get();

    String otpMessage = "Your BayAni OTP is: " + code + ". Use it to complete your forgot password request. Do not share this code with anyone.";
    try {
      String requestBody = "{\n" +
          "    \"messages\": [\n" +
          "        {\n" +
          "            \"destinations\": [\n" +
          "                {\n" +
          "                    \"to\": \"" + number + "\"\n" +
          "                }\n" +
          "            ],\n" +
          "            \"from\": \"" + System.getenv("SMS_FROM") + "\",\n" +
          "            \"text\": \"" + otpMessage + "\"\n" +
          "        }\n" +
          "    ]\n" +
          "}";

      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.add(AUTHORIZATION, "App " + System.getenv("SMS_API_KEY"));
      HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

      ResponseEntity<String> response = restTemplate.exchange(
          System.getenv("SMS_URL"),
          HttpMethod.POST,
          entity,
          String.class);
      log.info("Response {}", response.getBody());

      OtpDTO otpDTO = new OtpDTO();
      otpDTO.setCode(code);
      otpDTO.setUserId(user.getUserId());
      otpDTO.setNumber(number);

      return otpDTO;
    } catch (Exception exception) {
      log.error("Failed to send OTP to:" + number, exception);
    }
    return null;
  }

  public OtpDTO requestVerifyPhoneNumberOTP(OtpDTO request) throws MessagingException, IOException {
    String email = request.getEmail();

    Boolean isExist = userRepo.existsByEmail(email);
    if (isExist) {
      return null;
    }

    Integer code = getCode();

    OtpDTO otpDTO = new OtpDTO();
    otpDTO.setCode(code);
    otpDTO.setEmail(email);

    return otpDTO;
  }

  public Reservation sendNoticationReservation(Reservation reservation) throws MessagingException, IOException {
    User user = reservation.getUser();

    String number = user.getPhoneNumber();

    Store store = reservation.getStore();

    Integer transactionId = reservation.getTransactionId();
    Optional<Transaction> transacOptional = transactionRepo.findByTransactionId(transactionId);
    if (transacOptional.isEmpty()) {
      return null;
    }
    Transaction transaction = transacOptional.get();

    String otpMessage = "Hi " + user.getFirstName() + ",\n" +
        "Your reservation has been approved. You can now proceed to " + store.getName() +
        " on your scheduled pickup date.\n" +
        "Transaction #: " + transaction.getTransactionNum() + "\n" +
        "Thank you!";

    try {
      String sanitizedOtpMessage = otpMessage.replace("\n", "\\n").replace("\"", "\\\"");

      String requestBody = "{\n" +
          "    \"messages\": [\n" +
          "        {\n" +
          "            \"destinations\": [\n" +
          "                {\n" +
          "                    \"to\": \"" + number + "\"\n" +
          "                }\n" +
          "            ],\n" +
          "            \"from\": \"" + System.getenv("SMS_FROM") + "\",\n" +
          "            \"text\": \"" + sanitizedOtpMessage + "\"\n" +
          "        }\n" +
          "    ]\n" +
          "}";

      log.info("Message: {}", requestBody);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.add(AUTHORIZATION, "App " + System.getenv("SMS_API_KEY"));
      HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

      ResponseEntity<String> response = restTemplate.exchange(
          System.getenv("SMS_URL"),
          HttpMethod.POST,
          entity,
          String.class);
      log.info("Response {}", response.getBody());

      return reservation;
    } catch (Exception exception) {
      log.error("Failed to send Notification to:" + number, exception);
    }
    return null;
  }

  public Reservation sendNoticationReservationRejected(Reservation reservation) throws MessagingException, IOException {
    User user = reservation.getUser();

    String number = user.getPhoneNumber();

    Integer transactionId = reservation.getTransactionId();
    Optional<Transaction> transacOptional = transactionRepo.findByTransactionId(transactionId);
    if (transacOptional.isEmpty()) {
      return null;
    }
    Transaction transaction = transacOptional.get();

    Store store = reservation.getStore();

    String otpMessage = "Hi " + user.getFirstName() + ". We regret to inform you that your reservation at " + store.getName() + " has been rejected.\n" +
        "Reason: " + reservation.getNote() + ". " +
        "Thank you for understanding.";

    log.info("Message: {}", otpMessage);

    try {
      String requestBody = "{\n" +
          "    \"messages\": [\n" +
          "        {\n" +
          "            \"destinations\": [\n" +
          "                {\n" +
          "                    \"to\": \"" + number + "\"\n" +
          "                }\n" +
          "            ],\n" +
          "            \"from\": \"" + System.getenv("SMS_FROM") + "\",\n" +
          "            \"text\": \"" + otpMessage.replace("\n", "\\n").replace("\"", "\\\"") + "\"\n" +
          "        }\n" +
          "    ]\n" +
          "}";

      log.info("Message: {}", requestBody);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.add(AUTHORIZATION, "App " + System.getenv("SMS_API_KEY"));
      HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

      ResponseEntity<String> response = restTemplate.exchange(
          System.getenv("SMS_URL"),
          HttpMethod.POST,
          entity,
          String.class);
      log.info("Response {}", response.getBody());

      return reservation;
    } catch (Exception exception) {
      log.error("Failed to send Notification to:" + number, exception);
    }
    return null;
  }

  private Integer getCode() {
    SecureRandom random = new SecureRandom();
    return 100000 + random.nextInt(900000);
  }
}