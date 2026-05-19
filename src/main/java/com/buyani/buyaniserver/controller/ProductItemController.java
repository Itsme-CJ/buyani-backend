package com.buyani.buyaniserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.buyani.buyaniserver.dto.ProductItemDTO;
import com.buyani.buyaniserver.entity.ProductItem;
import com.buyani.buyaniserver.service.ProductItemService;

@RestController
@RequestMapping("/api/productItem")
public class ProductItemController {

  @Autowired
  ProductItemService productItemService;

  @GetMapping("/all")
  public ResponseEntity<List<ProductItem>> getAllProducts() {
    return new ResponseEntity<>(productItemService.getAllProducts(), HttpStatus.OK);
  }

  @GetMapping("/active")
  public ResponseEntity<List<ProductItem>> getActiveProducts() {
    return new ResponseEntity<>(productItemService.getActiveProducts(), HttpStatus.OK);
  }

  @GetMapping("/store/{storeId}")
  public ResponseEntity<List<ProductItem>> getProductsByStoreId(@PathVariable("storeId") Integer storeId) {
    return new ResponseEntity<>(productItemService.getProductsByStoreId(storeId), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Map<String, Object>> getProductById(@PathVariable("id") Integer id) {
    return new ResponseEntity<>(productItemService.getProductById(id), HttpStatus.OK);
  }

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

  @PatchMapping("/{id}/stock/decrement")
  public ResponseEntity<Object> decrementStock(
      @PathVariable("id") Integer id,
      @RequestParam("qty") Integer qty) {
    productItemService.decrementStock(id, qty);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<Object> updateProductItemStatus(
      @PathVariable("id") Integer id,
      @RequestParam("status") String status) {
    productItemService.updateProductItemStatus(id, status);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteProductItem(@PathVariable("id") Integer id) {
    productItemService.deleteProductItem(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
