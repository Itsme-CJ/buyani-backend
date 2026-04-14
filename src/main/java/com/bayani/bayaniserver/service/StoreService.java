package com.bayani.bayaniserver.service;

import java.io.IOException;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.bayani.bayaniserver.dto.AccountDTO;
import com.bayani.bayaniserver.dto.StoreDTO;
import com.bayani.bayaniserver.dto.UserDTO;
import com.bayani.bayaniserver.entity.Store;
import com.bayani.bayaniserver.entity.User;
import com.bayani.bayaniserver.repository.StoreRepo;
import com.bayani.bayaniserver.repository.UserRepo;
import com.bayani.bayaniserver.util.StorageUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StoreService {

  @Autowired
  StoreRepo storeRepo;

  @Autowired
  EmailService emailService;

  @Autowired
  VerificationService verificationService;

  @Autowired
  UserService userService;

  @Autowired
  UserRepo userRepo;

  public Store applyStore(StoreDTO request) throws MessagingException, IOException {
    Boolean isExist = storeRepo.existsByName(request.getName());
    if (isExist) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Store name already exist!");
    }

    Store store = new Store();
    store.setName(request.getName());
    store.setStatus("PENDING");
    store.setFirstAddress(request.getFirstAddress());
    store.setSecondAddress(request.getSecondAddress());
    store.setCity(request.getCity());
    store.setState(request.getState());
    store.setPostalCode(request.getPostalCode());
    store.setEmail(request.getEmail());
    store.setPhoneNumber(request.getPhoneNumber());
    store.setDescription(request.getDescription());
    store.setIsReservationActivated(0);

    store = storeRepo.save(store);

    if (request.getUserId() != null) {
      Optional<User> userOptional = userRepo.findByUserId(request.getUserId());
      if (userOptional.isPresent()) {
        User user = userOptional.get();
        user.setStoreId(String.valueOf(store.getStoreId()));
        userRepo.save(user);
      }
    }

    return store;
  }

  public Store createStore(StoreDTO request) throws MessagingException, IOException {
    Boolean isExist = storeRepo.existsByName(request.getName());
    if (isExist) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Store name already exist!");
    }

    Boolean isUserExist = userRepo.existsByEmail(request.getUser().getEmail());
    if (isUserExist) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email address already exist!");
    }

    Store store = new Store();
    store.setName(request.getName());
    store.setStatus("1");
    store.setIsReservationActivated(1);
    store.setFirstAddress(request.getFirstAddress());
    store.setCity(request.getCity());
    store.setDescription(request.getDescription());
    store.setEmail(request.getEmail());
    store.setSecondAddress(request.getSecondAddress());
    store.setPhoneNumber(request.getPhoneNumber());
    store.setState(request.getState());
    store.setPinLocation(request.getPinLocation());
    store.setPostalCode(request.getPostalCode());

    store = storeRepo.save(store);
    UserDTO user = request.getUser();
    user.setStoreId(String.valueOf(store.getStoreId()));
    user.setRole("PADMIN");
    userService.createUser(user);
    return store;
  }

  public ResponseEntity<Object> updateStoreStatus(Integer id, String status) {
    Optional<Store> storeOptional = storeRepo.findByStoreId(id);
    if (storeOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Store not found!");
    }
    Store store = storeOptional.get();
    store.setStatus(status);
    storeRepo.save(store);
    return new ResponseEntity<>(store, HttpStatus.OK);
  }

  public ResponseEntity<Object> updateStoreAccount(Integer id, AccountDTO request, MultipartFile qrCode) throws MessagingException, IOException {
    Optional<Store> storeOptional = storeRepo.findByStoreId(id);
    if (storeOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Store not exist!");
    }

    try {
      Store store = storeOptional.get();
      log.info("qrCode {}", qrCode);
      if (qrCode != null) {
        StorageUtil storageUtil = new StorageUtil();
        String fileName = storageUtil.getImagePathName(qrCode);
        log.info("fileName {}", fileName);
        if (!fileName.equals(store.getQrCode())) {
          String path = storageUtil.uploadImage(qrCode);
          log.info("path {}", path);
          store.setQrCode(path);
        }
      }
      store.setAccountName(request.getAccountName());
      store.setAccountNumber(request.getAccountNumber());

      store = storeRepo.save(store);
      return new ResponseEntity<>("", HttpStatus.OK);
    } catch (Exception e) {
      log.error("Error  {}", e.getMessage());
    }
    return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
  }
}