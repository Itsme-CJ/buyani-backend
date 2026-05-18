package com.buyani.buyaniserver.controller;

import java.io.IOException;
import java.util.Optional;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.buyani.buyaniserver.dto.AccountDTO;
import com.buyani.buyaniserver.dto.StoreDTO;
import com.buyani.buyaniserver.entity.Store;
import com.buyani.buyaniserver.repository.StoreRepo;
import com.buyani.buyaniserver.service.StoreService;

@RestController
@RequestMapping("/api/store")
public class StoreController {

  @Autowired
  StoreRepo storeRepo;

  @Autowired
  StoreService storeService;

  @PostMapping(value = "/apply", consumes = {"multipart/form-data"})
  public ResponseEntity<Object> applyStore(
      @ModelAttribute StoreDTO request,
      @RequestParam(name = "image", required = false) MultipartFile image
  ) throws MessagingException, IOException {
    Store response = storeService.applyStore(request, image);
    if (response != null) {
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    return new ResponseEntity<>("BadRequest", HttpStatus.BAD_REQUEST);
  }

  @PatchMapping("/{id}/approve")
  public ResponseEntity<Object> approveStore(@PathVariable("id") Integer id) {
    return storeService.updateStoreStatus(id, "APPROVED");
  }

  @PatchMapping("/{id}/reject")
  public ResponseEntity<Object> rejectStore(@PathVariable("id") Integer id) {
    return storeService.updateStoreStatus(id, "REJECTED");
  }

  @PostMapping("/create")
  public ResponseEntity<Object> createStore(@RequestBody StoreDTO request) throws MessagingException, IOException {
    Store response = storeService.createStore(request);
    if (response != null) {
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    return new ResponseEntity<>("BadRequest", HttpStatus.BAD_REQUEST);
  }

  @PatchMapping(value = "/account/{id}", consumes = {"multipart/form-data"})
  public ResponseEntity<Object> updateStoreAccount(
      @PathVariable("id") Integer id,
      @ModelAttribute AccountDTO request,
      @RequestParam(name = "image", required = false) MultipartFile qrCode
  ) throws MessagingException, IOException {
    return storeService.updateStoreAccount(id, request, qrCode);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getStore(@PathVariable("id") Integer id) {
    Optional<Store> store = storeRepo.findByStoreId(id);
    if (store.isPresent()) {
      return new ResponseEntity<>(store.get(), HttpStatus.OK);
    }
    return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
  }

  @GetMapping("/search/findByEmail")
  public ResponseEntity<Object> findByEmail(@RequestParam String email) {
    List<Store> stores = storeRepo.findByEmail(email);
    if (!stores.isEmpty()) {
      return new ResponseEntity<>(stores.get(0), HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @GetMapping("/search/findByName")
  public ResponseEntity<Object> findByName(@RequestParam String name) {
    return storeRepo.findByName(name)
      .<ResponseEntity<Object>>map(s -> new ResponseEntity<>(s, HttpStatus.OK))
      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping("/search/findByStatus")
  public ResponseEntity<Object> findByStatus(@RequestParam String status) {
    List<Store> stores = storeRepo.findByStatus(status);
    return new ResponseEntity<>(stores, HttpStatus.OK);
  }

  @DeleteMapping("/{id}") // 👈 added
  public ResponseEntity<Object> deleteStore(@PathVariable("id") Integer id) {
    return storeService.deleteStore(id);
  }
}