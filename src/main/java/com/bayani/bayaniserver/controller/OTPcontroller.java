package com.bayani.bayaniserver.controller;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bayani.bayaniserver.dto.OtpDTO;
import com.bayani.bayaniserver.service.OTPService;

@RestController
@RequestMapping("/api/otp")
public class OTPcontroller {
  @Autowired
  OTPService otpService;

  @PostMapping("/verify")
  public ResponseEntity<Object> createUser(@RequestBody OtpDTO request) throws MessagingException, IOException {
    OtpDTO response = otpService.requestVerifyPhoneNumberOTP(request);
    if (response != null) {
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    return new ResponseEntity<>("", HttpStatus.OK);
  }

  @PostMapping("/forgot-password-verify")
  public ResponseEntity<Object> forgotPasswordUser(@RequestBody OtpDTO request) throws MessagingException, IOException {
    OtpDTO response = otpService.requestForgotPasswordOTP(request);
    if (response != null) {
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    return new ResponseEntity<>("", HttpStatus.OK);
  }

}
