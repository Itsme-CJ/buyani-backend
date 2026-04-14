package com.bayani.bayaniserver.service;

import java.io.IOException;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bayani.bayaniserver.dto.UserDTO;
import com.bayani.bayaniserver.entity.Role;
import com.bayani.bayaniserver.entity.User;
import com.bayani.bayaniserver.repository.RoleRepo;
import com.bayani.bayaniserver.repository.StoreRepo;
import com.bayani.bayaniserver.repository.UserRepo;

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

  public User findUserByToken() {
    String name = SecurityContextHolder.getContext().getAuthentication().getName();
    Optional<User> userOptional = userRepo.findByEmail(name);
    if (userOptional.isEmpty()) {
      return null;
    }
    return userOptional.get();
  }

  public Role getUserRole(Integer userid) {
    Optional<Role> optionalRole = roleRepo.findByUserUserId(userid);
    if (optionalRole.isEmpty()) {
      log.error("Not found");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return optionalRole.get();
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
}