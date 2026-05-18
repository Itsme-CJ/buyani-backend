package com.buyani.buyaniserver.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.buyani.buyaniserver.dto.UserDTO;
import com.buyani.buyaniserver.entity.Role;
import com.buyani.buyaniserver.entity.User;
import com.buyani.buyaniserver.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

  @Autowired
  UserService userService;

  @GetMapping("/role/{id}")
  public ResponseEntity<Object> getUserRole(@PathVariable Integer id) {
    try {
      Role response = userService.getUserRole(id);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (ResponseStatusException ex) {
      Map<String, String> error = new HashMap<>();
      error.put("message", "Role not found.");
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/monthly-stats")
  public ResponseEntity<Object> getMonthlyStats() {
    try {
      Map<String, Object> stats = userService.getMonthlySignupStats();
      return new ResponseEntity<>(stats, HttpStatus.OK);
    } catch (Exception ex) {
      Map<String, String> error = new HashMap<>();
      error.put("message", "Failed to fetch stats.");
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/find-by-email")
  public ResponseEntity<Object> findByEmail(@RequestParam String email) {
    try {
      Optional<User> user = userService.findByEmail(email.trim().toLowerCase());
      if (user.isEmpty()) {
        Map<String, String> error = new HashMap<>();
        error.put("message", "User not found.");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(user.get(), HttpStatus.OK);
    } catch (Exception ex) {
      Map<String, String> error = new HashMap<>();
      error.put("message", "Something went wrong.");
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/customers")
  public ResponseEntity<Object> getCustomers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    try {
      Page<User> result = userService.getCustomers(page, size);
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception ex) {
      Map<String, String> error = new HashMap<>();
      error.put("message", "Failed to fetch customers.");
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/create")
  public ResponseEntity<Object> createUser(@RequestBody UserDTO request) throws MessagingException, IOException {
    try {
      User response = userService.createUser(request);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (ResponseStatusException ex) {
      Map<String, String> error = new HashMap<>();
      error.put("message", resolveConflictMessage(ex.getReason()));
      return new ResponseEntity<>(error, ex.getStatus());
    }
  }

  @PostMapping("/create-cx")
  public ResponseEntity<Object> createUserCustomer(@RequestBody UserDTO request) throws MessagingException, IOException {
    try {
      User response = userService.createUserCustomer(request);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (ResponseStatusException ex) {
      String reason = ex.getReason();
      String field = resolveField(reason);
      String message = resolveConflictMessage(reason);

      Map<String, String> error = new HashMap<>();
      error.put("field", field);
      error.put("message", message);
      return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
  }

  @PatchMapping("/update")
  public ResponseEntity<Object> updateUser(@RequestBody UserDTO request) {
    try {
      User response = userService.updateUser(request);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (ResponseStatusException ex) {
      Map<String, String> error = new HashMap<>();
      error.put("message", "User not found.");
      return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
  }

  private String resolveField(String reason) {
    if (reason == null) return "general";
    switch (reason) {
      case "EMAIL_EXISTS":    return "email";
      case "PHONE_EXISTS":    return "phoneNumber";
      default:                return "general";
    }
  }

  private String resolveConflictMessage(String reason) {
    if (reason == null) return "Something went wrong. Please try again.";
    switch (reason) {
      case "EMAIL_EXISTS":    return "Email address already exists.";
      case "PHONE_EXISTS":    return "Phone number already exists.";
      case "DUPLICATE_ENTRY": return "A duplicate entry was found.";
      default:                return "Something went wrong. Please try again.";
    }
  }
}