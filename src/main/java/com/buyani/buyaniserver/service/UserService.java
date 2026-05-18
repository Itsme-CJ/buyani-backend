package com.buyani.buyaniserver.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.buyani.buyaniserver.dto.UserDTO;
import com.buyani.buyaniserver.entity.Role;
import com.buyani.buyaniserver.entity.User;
import com.buyani.buyaniserver.repository.RoleRepo;
import com.buyani.buyaniserver.repository.StoreRepo;
import com.buyani.buyaniserver.repository.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  StoreRepo storeRepo;

  @Autowired
  RoleRepo roleRepo;

  @Autowired
  EmailService emailService;

  @Autowired
  VerificationService verificationService;

  @Autowired
  EmailVerificationService emailVerificationService;

  public User findUserByToken() {
    String name = SecurityContextHolder.getContext().getAuthentication().getName();
    Optional<User> userOptional = userRepo.findByEmail(name);
    if (userOptional.isEmpty()) {
      return null;
    }
    return userOptional.get();
  }

  public Optional<User> findByEmail(String email) {
    return userRepo.findByEmail(email);
  }

  public Role getUserRole(Integer userid) {
    Optional<Role> optionalRole = roleRepo.findByUserUserId(userid);
    if (optionalRole.isEmpty()) {
      log.error("Not found");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return optionalRole.get();
  }

  public Page<User> getCustomers(int page, int size) {
    PageRequest pageable = PageRequest.of(page, size);
    return userRepo.findByRoleName("CUSTOMER", pageable);
  }

  public User createUserCustomer(UserDTO userDTO) throws MessagingException, IOException {
    if (userRepo.existsByEmail(userDTO.getEmail())) {
      log.error("Email address already exists");
      throw new ResponseStatusException(HttpStatus.CONFLICT, "EMAIL_EXISTS");
    }

    User user = new User();
    user.setEmail(userDTO.getEmail());
    user.setFirstName(userDTO.getFirstName());
    user.setLastName(userDTO.getLastName());
    user.setPhoneNumber(userDTO.getPhoneNumber());

    String rolename = userDTO.getRole();
    Optional<Role> optionalRole = roleRepo.findByName(rolename);
    if (!optionalRole.isEmpty()) {
      Role role = optionalRole.get();
      user.setRole(role);
      user.setRoleId(role.getRoleId());
    }

    if (userDTO.getPassword() != null && !userDTO.getPassword().equals("")) {
      BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    }

    try {
      userRepo.save(user);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "DUPLICATE_ENTRY");
    }

    try {
      emailVerificationService.sendVerificationCode(user.getEmail());
      log.info("Verification email triggered for {}", user.getEmail());
    } catch (Exception e) {
      log.error("Could not send verification email to {}: {}", user.getEmail(), e.getMessage());
    }

    return user;
  }

  public User createUser(UserDTO userDTO) throws MessagingException, IOException {
    User user = new User();
    user.setEmail(userDTO.getEmail());
    user.setFirstName(userDTO.getFirstName());
    user.setLastName(userDTO.getLastName());
    user.setPhoneNumber(userDTO.getPhoneNumber());
    user.setStoreId(userDTO.getStoreId());

    String rolename = userDTO.getRole();
    Optional<Role> optionalRole = roleRepo.findByName(rolename);
    if (!optionalRole.isEmpty()) {
      Role role = optionalRole.get();
      user.setRole(role);
      user.setRoleId(role.getRoleId());
    }

    if (userDTO.getPassword() != null && !userDTO.getPassword().equals("")) {
      BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    }

    try {
      userRepo.save(user);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "DUPLICATE_ENTRY");
    }

    return user;
  }

  public User updateUser(UserDTO userDTO) {
    Integer userid = userDTO.getUserId();
    Optional<User> optionalUser = userRepo.findByUserId(userid);
    if (optionalUser.isEmpty()) {
      log.error("Invalid user info");
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    User user = optionalUser.get();
    user.setEmail(userDTO.getEmail());
    user.setFirstName(userDTO.getFirstName());
    user.setLastName(userDTO.getLastName());
    user.setPhoneNumber(userDTO.getPhoneNumber());

    String rolename = userDTO.getRole();
    Optional<Role> optionalRole = roleRepo.findByName(rolename);
    if (!optionalRole.isEmpty()) {
      Role role = optionalRole.get();
      user.setRole(role);
      user.setRoleId(role.getRoleId());
    }

    userRepo.save(user);
    return user;
  }

  public Map<String, Object> getMonthlySignupStats() {
    int year = LocalDate.now().getYear();

    int[] sellerData   = new int[12];
    int[] customerData = new int[12];

    List<Object[]> sellerRows = userRepo.countMonthlySignupsByRole("SELLER", year);
    for (Object[] row : sellerRows) {
      int month = ((Number) row[0]).intValue();
      int count = ((Number) row[1]).intValue();
      sellerData[month - 1] = count;
    }

    List<Object[]> customerRows = userRepo.countMonthlySignupsByRole("CUSTOMER", year);
    for (Object[] row : customerRows) {
      int month = ((Number) row[0]).intValue();
      int count = ((Number) row[1]).intValue();
      customerData[month - 1] = count;
    }

    long totalSellers   = userRepo.countByRoleName("SELLER");
    long totalCustomers = userRepo.countByRoleName("CUSTOMER");

    Map<String, Object> result = new HashMap<>();
    result.put("sellers",        sellerData);
    result.put("customers",      customerData);
    result.put("totalSellers",   totalSellers);
    result.put("totalCustomers", totalCustomers);
    return result;
  }
}