package com.buyani.buyaniserver.controller;

import javax.ws.rs.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buyani.buyaniserver.dto.ProductCategoryDTO;
import com.buyani.buyaniserver.entity.ProductCategory;
import com.buyani.buyaniserver.service.ProductCategorySevice;

@RestController
@RequestMapping("/api/productCategory")
public class ProductCategoriesController {

  @Autowired
  ProductCategorySevice productCategorySevice;

  @PostMapping("/create")
  public ResponseEntity<Object> createProductCategory(@RequestBody ProductCategoryDTO request) {
    ProductCategory response = productCategorySevice.createProductCategory(request);
    if (response != null) {
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    return new ResponseEntity<>("BadRequest", HttpStatus.BAD_REQUEST);
  }

  @PatchMapping("/update")
  public ResponseEntity<Object> updateProductCategory(@RequestBody ProductCategoryDTO request) {
    ProductCategory response = productCategorySevice.updateProductCategory(request);
    if (response != null) {
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    return new ResponseEntity<>("BadRequest", HttpStatus.BAD_REQUEST);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteProductCategory(@PathVariable Integer id) {
    ProductCategory response = productCategorySevice.deleteProductCategory(id);
    if (response != null) {
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    return new ResponseEntity<>("BadRequest", HttpStatus.BAD_REQUEST);
  }

  
}
