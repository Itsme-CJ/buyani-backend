package com.bayani.bayaniserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import com.bayani.bayaniserver.dto.ProductCategoryDTO;
import com.bayani.bayaniserver.dto.ProductItemDTO;
import com.bayani.bayaniserver.entity.ProductCategory;
import com.bayani.bayaniserver.entity.ProductItem;
import com.bayani.bayaniserver.entity.Store;
import com.bayani.bayaniserver.entity.User;
import com.bayani.bayaniserver.repository.ProductCategoryRepo;
import com.bayani.bayaniserver.repository.ProductItemRepo;
import com.bayani.bayaniserver.repository.StoreRepo;
import com.bayani.bayaniserver.repository.UserRepo;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ProductItemService {
  @Autowired
  ProductItemRepo productItemRepo;

  @Autowired
  ProductCategoryRepo productCategoryRepo;

  @Autowired
  UserRepo userRepo;

  @Autowired
  StoreRepo storeRepo;
 
  public ProductItem createProductItem(ProductItemDTO productItemDTO) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    Optional<User> userOptional = userRepo.findByEmail(username);
    if (userOptional.isEmpty()) {
      log.error("Invalid user info");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    User user = userOptional.get();

    String storeId = user.getStoreId();

    Optional<Store> storeOptional = storeRepo.findByStoreId(Integer.parseInt(storeId));
    if (storeOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    String number = productItemDTO.getProductNumber();
    Optional<ProductItem> prodOptional = productItemRepo.findByProductNumber(number);
    if (prodOptional.isPresent()) {
      Integer prodStoreId = prodOptional.get().getStoreId();

      if (prodStoreId != null && prodStoreId == Integer.parseInt(storeId)) {
        log.info("Product Item already exist");
        throw new ResponseStatusException(HttpStatus.CONFLICT);
      }
    }

    ProductItem productItem = new ProductItem();
    productItem.setDescription(productItemDTO.getDescription());
    productItem.setName(productItemDTO.getName());
    productItem.setPrice(productItemDTO.getPrice());
    productItem.setProductNumber(productItemDTO.getProductNumber());
    productItem.setStoreId(Integer.parseInt(storeId));
    productItem.setStock(productItemDTO.getStock());
    productItem.setCriticalLevel(productItemDTO.getCriticalLevel());
    productItem.setGenericName(productItemDTO.getGenericName());
    productItem.setWithPrescription(productItemDTO.getWithPrescription());

    String productCategoryName = productItemDTO.getCategory();
    Optional<ProductCategory> prodCategoryOptional = productCategoryRepo.findByNameAndStoreStoreId(productCategoryName , Integer.parseInt(storeId));
    if (prodCategoryOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    ProductCategory productCategory = prodCategoryOptional.get();
    productItem.setProductCategory(productCategory);

    Integer newItemNumber = productCategory.getItemNumber() + 1;
    productCategory.setItemNumber(newItemNumber);
    productCategoryRepo.save(productCategory);
    
    productItemRepo.save(productItem);

    return productItem;
  } 
  
  public ProductItem updateProductItem(ProductItemDTO productItemDTO) {
    Integer productItemId = productItemDTO.getProductItemId();
    Optional<ProductItem> prodOptional = productItemRepo.findByProductItemId(productItemId);

    if (prodOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    ProductItem productItem = prodOptional.get();
    productItem.setDescription(productItemDTO.getDescription());
    productItem.setName(productItemDTO.getName());
    productItem.setProductNumber(productItemDTO.getProductNumber());
    productItem.setPrice(productItemDTO.getPrice());
    productItem.setStock(productItemDTO.getStock());
    productItem.setCriticalLevel(productItemDTO.getCriticalLevel());
    productItem.setGenericName(productItemDTO.getGenericName());
    productItem.setWithPrescription(productItemDTO.getWithPrescription());

    String productCategoryName = productItemDTO.getCategory();
    Optional<ProductCategory> prodCategoryOptional = productCategoryRepo.findByNameAndStoreStoreId(productCategoryName, prodOptional.get().getStoreId());
    if (prodCategoryOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    ProductCategory productCategory = prodCategoryOptional.get();
    ProductCategory productCategory2 = productItem.getProductCategory();
    Integer categoryId = productCategory.getProductCategoryId();
    Integer categoryId2 = productCategory2.getProductCategoryId();

    if (categoryId != categoryId2) {
      productItem.setProductCategory(productCategory);
  
      Integer newItemNumber = productCategory.getItemNumber() + 1;
      Integer newItemNumber2 = productCategory2.getItemNumber() - 1;

      productCategory.setItemNumber(newItemNumber);
      productCategory2.setItemNumber(newItemNumber2);
      productCategoryRepo.save(productCategory);
      productCategoryRepo.save(productCategory2);
    }

    productItemRepo.save(productItem);
    return productItem;
  } 
}
