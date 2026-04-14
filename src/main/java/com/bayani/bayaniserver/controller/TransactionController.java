package com.bayani.bayaniserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bayani.bayaniserver.dto.TransactionDTO;
import com.bayani.bayaniserver.entity.Transaction;
import com.bayani.bayaniserver.service.TransactionService;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
  @Autowired
  TransactionService transactionService;

  @PostMapping("/create")
  public ResponseEntity<Object> createProductCategory(@RequestBody TransactionDTO request) {
    Transaction response = transactionService.createTransaction(request);
    if (response != null) {
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    return new ResponseEntity<>("BadRequest", HttpStatus.BAD_REQUEST);
  }

  @PatchMapping("/update")
  public ResponseEntity<Object> updateProductCategory(@RequestBody TransactionDTO request) {
    Transaction response = transactionService.updateTransaction(request);
    if (response != null) {
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    return new ResponseEntity<>("BadRequest", HttpStatus.BAD_REQUEST);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getProductCategory(@PathVariable Integer id) {
    TransactionDTO response = transactionService.getTransaction(id);
    if (response != null) {
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    return new ResponseEntity<>("BadRequest", HttpStatus.BAD_REQUEST);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Object> voidTransaction(@PathVariable Integer id, @RequestBody TransactionDTO request) {
    Transaction response = transactionService.voidTransaction(request, id);
    if (response != null) {
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    return new ResponseEntity<>("BadRequest", HttpStatus.BAD_REQUEST);
  }
}
