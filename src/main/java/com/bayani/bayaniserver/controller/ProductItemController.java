package com.bayani.bayaniserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bayani.bayaniserver.dto.ProductItemDTO;
import com.bayani.bayaniserver.entity.ProductItem;
import com.bayani.bayaniserver.service.ProductItemService;

@RestController
@RequestMapping("/api/productItem")
public class ProductItemController {

  @Autowired
  ProductItemService productItemService;

  @PostMapping("/create")
  public ResponseEntity<Object> createProductCategory(@RequestBody ProductItemDTO request) {
    ProductItem response = productItemService.createProductItem(request);
    if (response != null) {
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    return new ResponseEntity<>("BadRequest", HttpStatus.BAD_REQUEST);
  }

  @PatchMapping("/update")
  public ResponseEntity<Object> updateProductCategory(@RequestBody ProductItemDTO request) {
    ProductItem response = productItemService.updateProductItem(request);
    if (response != null) {
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    return new ResponseEntity<>("BadRequest", HttpStatus.BAD_REQUEST);
  }

  
}
